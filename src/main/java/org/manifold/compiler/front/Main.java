package org.manifold.compiler.front;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.manifold.compiler.BooleanTypeValue;
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

    // cheating with BOTH hands.
    // TODO ...something about this
    defaultNamespace.getPrivateScope().defineVariable(
        new VariableIdentifier(defaultNamespaceID, "Bool"));
    defaultNamespace.getPrivateScope().assignVariable(
        new VariableIdentifier(defaultNamespaceID, "Bool"),
        new LiteralExpression(BooleanTypeValue.getInstance()));

    log.debug("top-level identifiers:");
    for (VariableIdentifier id :
        defaultNamespace.getPrivateScope().getSymbolIdentifiers()) {
      log.debug(id);
    }

    // let's see if we can do this before static type-checking
    ExpressionGraphBuilder exprGraphBuilder = new ExpressionGraphBuilder(
        expressions, namespaces);
    ExpressionGraph exprGraph = exprGraphBuilder.build();

    log.debug("writing out expression graph");
    File exprGraphDot = new File(inputFile.getName() + ".exprs.dot");
    exprGraph.writeDOTFile(exprGraphDot);

    /*
    TypeChecker.typecheck(namespaces, defaultNamespace);
    log.debug("assigned the following types:");
    for (VariableIdentifier id :
        defaultNamespace.getPrivateScope().getSymbolIdentifiers()) {
      NamespaceIdentifier name = id.getNamespaceIdentifier();
      Namespace ns = namespaces.get(name);
      Variable v = ns.getPrivateScope().getVariable(id);
      log.debug(v.getIdentifier() + " ::= " + v.getType().toString());
    }

    ExpressionGraph exprGraph = new ExpressionGraph(
        defaultNamespace.getPrivateScope());
    exprGraph.buildFrom(expressions);
    exprGraph.removeUnconnectedEdges();
    exprGraph.optimizeOutVariables();

    exprGraph.elaboratePrimitives();
    log.debug("instantiated primitives:");
    for (String s : exprGraph.getPrintableInstances()) {
      log.debug(s);
    }
    exprGraph.elaborateConnections(schematic);
    exprGraph.writeSchematic(schematic);
    */

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
