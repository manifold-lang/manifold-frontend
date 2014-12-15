package org.manifold.compiler.front;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.manifold.compiler.BooleanValue;
import org.manifold.compiler.Frontend;
import org.manifold.compiler.IntegerValue;
import org.manifold.compiler.UndefinedBehaviourError;
import org.manifold.compiler.middle.Schematic;
import org.manifold.parser.ManifoldBaseVisitor;
import org.manifold.parser.ManifoldLexer;
import org.manifold.parser.ManifoldParser;
import org.manifold.parser.ManifoldParser.ExpressionContext;
import org.manifold.parser.ManifoldParser.FunctionTypeValueContext;
import org.manifold.parser.ManifoldParser.NamespacedIdentifierContext;
import org.manifold.parser.ManifoldParser.TupleTypeValueContext;
import org.manifold.parser.ManifoldParser.TupleTypeValueEntryContext;
import org.manifold.parser.ManifoldParser.TupleValueContext;
import org.manifold.parser.ManifoldParser.TupleValueEntryContext;
import org.manifold.parser.ManifoldParser.TypevalueContext;

public class Main implements Frontend {

  private static Logger log = LogManager.getLogger("DefaultFrontend");

  public Main() {}

  @Override
  public String getFrontendName() {
    return "default";
  }

  @Override
  public void registerArguments(Options options) {
    // TODO Auto-generated method stub

  }

  public static void elaborateFunctions(ExpressionGraph g) throws Exception {
    // Maintain a set of unelaborated function invocations and
    // iterate until this set is empty.

    Set<FunctionInvocationVertex> funcalls = new LinkedHashSet<>();
    // Add all function invocations initially present in the graph
    for (ExpressionVertex v : g.getNonVariableVertices()) {
      if (v instanceof FunctionInvocationVertex) {
        funcalls.add((FunctionInvocationVertex) v);
      }
    }

    // now proceed
    while (!(funcalls.isEmpty())) {
      // get next vertex
      Iterator<FunctionInvocationVertex> iterator = funcalls.iterator();
      FunctionInvocationVertex v = iterator.next();
      funcalls.remove(v);
      v.elaborate();
      // TODO it would be more efficient for the vertex to tell us whether
      // any new function invocations were created during elaboration
      for (ExpressionVertex vNew : g.getNonVariableVertices()) {
        if (vNew instanceof FunctionInvocationVertex) {
          funcalls.add((FunctionInvocationVertex) vNew);
        }
      }
    }
  }

  @Override
  public Schematic invokeFrontend(CommandLine cmd) throws Exception {

    File inputFile = Paths.get(cmd.getArgs()[0]).toFile();

    ManifoldLexer lexer = new ManifoldLexer(new ANTLRInputStream(
        new FileInputStream(inputFile)));

     // Get a list of matched tokens
    CommonTokenStream tokens = new CommonTokenStream(lexer);

    // Pass the tokens to the parser
    ManifoldParser parser = new ManifoldParser(tokens);

    // Specify our entry point
    ManifoldParser.SchematicContext context = parser.schematic();

    ExpressionContextVisitor visitor = new ExpressionContextVisitor();

    List<Expression> expressions = new LinkedList<>();
    List<ExpressionContext> expressionContexts = context.expression();

    for (ExpressionContext expressionContext : expressionContexts) {
      expressions.add(visitor.visit(expressionContext));
    }

    log.debug("expressions:");
    for (Expression expr : expressions) {
      log.debug(expr.toString());
    }

    Map<NamespaceIdentifier, Namespace> namespaces = new HashMap<>();

    NamespaceIdentifier defaultNamespaceID = new NamespaceIdentifier("");
    Namespace defaultNamespace = new Namespace(defaultNamespaceID);
    namespaces.put(defaultNamespaceID, defaultNamespace);
    // "top-level" bindings go into the private scope of the default namespace

    Schematic schematic = new Schematic(inputFile.getName());

    // Build top-level scope
    for (Expression expr : expressions) {
      if (expr instanceof VariableAssignmentExpression) {
        VariableAssignmentExpression assign =
            (VariableAssignmentExpression) expr;
        Expression lvalue = assign.getLvalueExpression();
        Expression rvalue = assign.getRvalueExpression();

        // the lvalue must be an expression we can assign to
        if (lvalue.isAssignable()) {
          // for now we are only handling the simplest case of
          // the lvalue being a single variable reference
          if (lvalue instanceof VariableReferenceExpression) {
            VariableReferenceExpression vRef =
                (VariableReferenceExpression) lvalue;
            VariableIdentifier identifier = vRef.getIdentifier();
            // Do not assign a type yet.
            defaultNamespace.getPrivateScope().defineVariable(
                identifier);
            defaultNamespace.getPrivateScope().assignVariable(
                identifier, rvalue);
          } else {
            throw new UndefinedBehaviourError(
                "unhandled lvalue type" + lvalue.getClass());
          }
        } else {
          throw new IllegalAssignmentException(lvalue);
        }
      }
    }

    log.debug("top-level identifiers:");
    for (VariableIdentifier id :
        defaultNamespace.getPrivateScope().getSymbolIdentifiers()) {
      log.debug(id);
    }

    // let's see if we can do this before static type-checking
    ExpressionGraphBuilder exprGraphBuilder = new ExpressionGraphBuilder(
        expressions, namespaces);
    ExpressionGraph exprGraph = exprGraphBuilder.build();

    // TODO expression graph correctness checks:
    // * all variables are assigned exactly once

    log.debug("writing out initial expression graph");
    File exprGraphDot = new File(inputFile.getName() + ".exprs.dot");
    exprGraph.writeDOTFile(exprGraphDot);

    elaborateFunctions(exprGraph);

    log.debug("writing out expression graph after function elaboration");
    File elaboratedDot = new File(inputFile.getName() + ".elaborated.dot");
    exprGraph.writeDOTFile(elaboratedDot);

    // TODO write out the schematic

    return schematic;

  }
}

class ExpressionContextVisitor extends ManifoldBaseVisitor<Expression> {

  @Override
  public Expression visitAssignmentExpression(
      ManifoldParser.AssignmentExpressionContext context) {
    return new VariableAssignmentExpression(
        visit(context.expression(0)),
        visit(context.expression(1))
    );
  }

  @Override
  public Expression visitFunctionInvocationExpression(
      ManifoldParser.FunctionInvocationExpressionContext context) {
    return new FunctionInvocationExpression (
        visit(context.expression(0)),
        visit(context.expression(1))
    );
  }

  // TODO KEY INSIGHT: combine the port type/port attributes and
  // node attributes in a single FunctionTypeValue signature.
  // As an example, if we have port types xIn(a: Int) and xOut(b: Int)
  // and want a node type xDev whose attributes are p,q: Bool,
  // input port u: xIn, output port v: xOut, we can declare it like
  //
  // xDev = primitive node (u: xIn, p: Bool, q: Bool) -> (v: xOut);
  //
  // and instantiate it like
  //
  // vResult = xDev(u: (0: uVal, 1: (a: 3)), p: True, q: False, v: (b: 4))
  //
  @Override
  public Expression visitPrimitiveNodeDefinitionExpression(
      ManifoldParser.PrimitiveNodeDefinitionExpressionContext context) {
    // extract port-mapping type
    FunctionTypeValueContext typeCtx = context.functionTypeValue();
    Expression typevalue = typeCtx.accept(this);
    // are there any attributes?
    if (context.tupleTypeValue() != null) {
      TupleTypeValueContext attributeTypesContext = context.tupleTypeValue();
      // TODO(murphy) the default expression visitor does not work here;
      // we need a visitTupleType() or visitTupleTypeValue()
      Expression attributes = attributeTypesContext.accept(this);
      return new PrimitiveNodeDefinitionExpression(typevalue, attributes);
    } else {
      return new PrimitiveNodeDefinitionExpression(typevalue);
    }
  }

  @Override
  public Expression visitPrimitivePortDefinitionExpression(
      ManifoldParser.PrimitivePortDefinitionExpressionContext context) {
    // extract signal type
    TypevalueContext typeCtx = context.typevalue();
    Expression typevalue = typeCtx.accept(this);
    // are there any attributes?
    if (context.tupleTypeValue() != null) {
      TupleTypeValueContext attributeTypesContext = context.tupleTypeValue();
      // TODO(murphy) the default expression visitor does not work here;
      // we need a visitTupleType() or visitTupleTypeValue()
      Expression attributes = attributeTypesContext.accept(this);
      return new PrimitivePortDefinitionExpression(typevalue, attributes);
    } else {
      return new PrimitivePortDefinitionExpression(typevalue);
    }
  }

  @Override
  public Expression visitTupleTypeValue(TupleTypeValueContext context) {
    // visit all children
    List<TupleTypeValueEntryContext> entries = context.tupleTypeValueEntry();
    Map<String, Expression> typeExprs = new HashMap<>();
    Map<String, Expression> defaultValues = new HashMap<>();
    for (TupleTypeValueEntryContext entryCtx : entries) {
      // each child has a typevalue, and may have
      // an identifier (named field)
      // and an expression (default value)
      Expression typevalue = entryCtx.typevalue().accept(this);
      String identifier;
      Integer nextAnonymousID = 0;
      if (entryCtx.IDENTIFIER() != null) {
        identifier = entryCtx.IDENTIFIER().getText();
      } else {
        // TODO verify this against the specification
        identifier = nextAnonymousID.toString();
        nextAnonymousID += 1;
      }
      typeExprs.put(identifier, typevalue);
      if (entryCtx.expression() != null) {
        Expression defaultValue = entryCtx.expression().accept(this);
        defaultValues.put(identifier, defaultValue);
      }
    }
    return new TupleTypeValueExpression(typeExprs, defaultValues);
  }

  @Override
  public Expression visitTupleValue(TupleValueContext context) {
    // visit all children
    List<TupleValueEntryContext> entries = context.tupleValueEntry();
    Map<String, Expression> valueExprs = new HashMap<>();
    for (TupleValueEntryContext entryCtx : entries) {
      // each child has a value, and may have an identifier (named field)
      Expression value = entryCtx.expression().accept(this);
      String identifier;
      Integer nextAnonymousID = 0;
      if (entryCtx.IDENTIFIER() != null) {
        identifier = entryCtx.IDENTIFIER().getText();
      } else {
        // TODO verify this against the specification
        identifier = nextAnonymousID.toString();
        nextAnonymousID += 1;
      }
      valueExprs.put(identifier, value);
    }
    return new TupleValueExpression(valueExprs);
  }

  @Override
  public Expression visitFunctionTypeValue(FunctionTypeValueContext context) {
    Expression inputExpr = context.tupleTypeValue(0).accept(this);
    Expression outputExpr = context.tupleTypeValue(1).accept(this);
    return new FunctionTypeValueExpression(inputExpr, outputExpr);
  }

  @Override
  public Expression visitNamespacedIdentifier(
      NamespacedIdentifierContext context) {

    List<TerminalNode> identifierNodes = context.IDENTIFIER();
    List<String> identifierStrings = new LinkedList<>();
    for (TerminalNode node : identifierNodes) {
      identifierStrings.add(node.getText());
    }

    VariableIdentifier variable = new VariableIdentifier(identifierStrings);

    return new VariableReferenceExpression(variable);
  }

  @Override
  public Expression visitTerminal(TerminalNode node) {
    if (node.getSymbol().getType() == ManifoldLexer.INTEGER_VALUE) {
      return new LiteralExpression(
          new IntegerValue(Integer.valueOf(node.getText()))
      );

    } else if (node.getSymbol().getType() == ManifoldLexer.BOOLEAN_VALUE) {
      return new LiteralExpression(
        BooleanValue.getInstance(Boolean.parseBoolean(node.getText()))
      );

    } else {
      throw new UndefinedBehaviourError(
          "unknown terminal node '" + node.getSymbol().getText() + "'");
    }
  }

}
