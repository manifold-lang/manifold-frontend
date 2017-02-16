package org.manifold.compiler.front;

import com.google.common.base.Throwables;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.manifold.compiler.*;
import org.manifold.parser.ManifoldBaseVisitor;
import org.manifold.parser.ManifoldLexer;
import org.manifold.parser.ManifoldParser.*;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

class ExpressionContextVisitor extends ManifoldBaseVisitor<ExpressionVertex> {

  private ExpressionGraph exprGraph;
  private File inputFile;
  private List<String> errors;

  public ExpressionGraph getExpressionGraph() {
    return this.exprGraph;
  }

  // Used to distinguish between unpacking and defining tuples
  private boolean isLHS = false;

  // Should tuples and variables be exported
  private boolean isPublic = false;

  private int nextTmpVar = 0;
  public ExpressionContextVisitor(File inputFile) {
    this(new ExpressionGraph(), inputFile);
  }

  public ExpressionContextVisitor(ExpressionGraph exprGraph, File inputFile) {
    this.inputFile = inputFile;
    this.exprGraph = exprGraph;
    this.errors = new ArrayList<>();
  }

  public List<String> getErrors() {
    return errors;
  }

  private File getLib(String libPath) {
    URL url = this.getClass().getClassLoader().getResource("libraries/" + libPath);
    if (url == null) {
      return null;
    }
    return new File(url.getFile());
  }

  @Override
  public ExpressionVertex visitAssignmentExpression(
      AssignmentExpressionContext context) {

    // get the vertex corresponding to the lvalue
    isLHS = true;
    isPublic = context.VISIBILITY_PUBLIC() != null;
    ExpressionVertex vLeft = context.lvalue().accept(this);

    // then get the rvalue...
    isLHS = false;
    isPublic = false;
    ExpressionVertex vRight = context.rvalue().accept(this);

    ExpressionEdge e = new ExpressionEdge(vRight, vLeft);
    exprGraph.addEdge(e);
    return vRight;
  }

  @Override
  public ExpressionVertex visitFunctionInvocationExpression(
      FunctionInvocationExpressionContext context) {
    // get the vertex corresponding to the function being called
    ExpressionVertex vFunction = context.reference().accept(this);
    ExpressionEdge eFunction = new ExpressionEdge(vFunction, null);
    // then get the input vertex
    ExpressionVertex vInput = context.rvalue().accept(this);
    ExpressionEdge eInput = new ExpressionEdge(vInput, null);

    FunctionInvocationVertex vInvocation = new FunctionInvocationVertex(
        exprGraph, eFunction, eInput);
    exprGraph.addVertex(vInvocation);
    exprGraph.addEdge(eFunction);
    exprGraph.addEdge(eInput);
    return vInvocation;
  }

  @Override
  public ExpressionVertex visitFunctionValue(
      FunctionValueContext ctx) {
    ExpressionContextVisitor functionGraphBuilder =
        new ExpressionContextVisitor(inputFile);
    ctx.expression().forEach(functionGraphBuilder::visit);
    ExpressionGraph fSubGraph = functionGraphBuilder.getExpressionGraph();

    ExpressionVertex fTypeVertex = visitFunctionTypeValue(
        ctx.functionTypeValue());
    ExpressionEdge fTypeEdge = new ExpressionEdge(fTypeVertex, null);

    FunctionValueVertex fValueVertex = new FunctionValueVertex(
        exprGraph, fTypeEdge, fSubGraph);
    exprGraph.addVertex(fValueVertex);
    exprGraph.addEdge(fTypeEdge);

    return fValueVertex;
  }

  // KEY INSIGHT: combine the port type/port attributes and
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
  public ExpressionVertex visitPrimitiveNodeDefinitionExpression(
      PrimitiveNodeDefinitionExpressionContext context) {
    ExpressionVertex vSignature = context.functionTypeValue().accept(this);
    ExpressionEdge eSignature = new ExpressionEdge(vSignature, null);
    exprGraph.addEdge(eSignature);
    PrimitiveNodeVertex vNode = new PrimitiveNodeVertex(exprGraph, eSignature);
    exprGraph.addVertex(vNode);
    return vNode;
  }

  @Override
  public ExpressionVertex visitPrimitivePortDefinitionExpression(
      PrimitivePortDefinitionExpressionContext context) {

    ExpressionVertex vSignalType = context.typevalue().accept(this);
    ExpressionEdge eSignalType = new ExpressionEdge(vSignalType, null);
    exprGraph.addEdge(eSignalType);

    ExpressionVertex vAttributes;
    if (context.tupleTypeValue() != null) {
      vAttributes = context.tupleTypeValue().accept(this);
    } else {
      vAttributes = new ConstantValueVertex(exprGraph,
          NilTypeValue.getInstance());
    }
    exprGraph.addVertex(vAttributes);
    ExpressionEdge eAttributes = new ExpressionEdge(vAttributes, null);
    exprGraph.addEdge(eAttributes);
    PrimitivePortVertex vPort = new PrimitivePortVertex(exprGraph,
        eSignalType, eAttributes);
    exprGraph.addVertex(vPort);
    return vPort;
  }

  @Override
  public ExpressionVertex visitTupleTypeValue(TupleTypeValueContext context) {
    List<TupleTypeValueEntryContext> entries = context.tupleTypeValueEntry();
    MappedArray<String, ExpressionEdge> typeValueEdges = new MappedArray<>();
    MappedArray<String, ExpressionEdge> defaultValueEdges = new MappedArray<>();
    Integer nextAnonymousID = 0;
    for (TupleTypeValueEntryContext entryCtx : entries) {
      // each child has a typevalue, and may have
      // an identifier (named field)
      // and an expression (default value)
      String identifier;
      if (entryCtx.IDENTIFIER() != null) {
        identifier = entryCtx.IDENTIFIER().getText();
      } else {
        identifier = nextAnonymousID.toString();
        nextAnonymousID += 1;
      }
      ExpressionVertex vxTypeValue = entryCtx.typevalue().accept(this);
      ExpressionEdge eTypeValue = new ExpressionEdge(vxTypeValue, null);
      typeValueEdges.put(identifier, eTypeValue);
      exprGraph.addEdge(eTypeValue);
      if (entryCtx.expression() != null) {
        ExpressionVertex vxDefaultValue = entryCtx.expression().accept(this);
        ExpressionEdge eDefaultValue = new ExpressionEdge(vxDefaultValue, null);
        defaultValueEdges.put(identifier, eDefaultValue);
        exprGraph.addEdge(eDefaultValue);
      }
    }
    TupleTypeValueVertex vTuple = new TupleTypeValueVertex(exprGraph,
        typeValueEdges, defaultValueEdges);
    exprGraph.addVertex(vTuple);
    return vTuple;
  }

  @Override
  public ExpressionVertex visitTupleValue(TupleValueContext context) {
    List<TupleValueEntryContext> entries = context.tupleValueEntry();

    // Desugar tuple unpacking to assignment to a temporary variable, then accessing the attributes of that variable
    if (isLHS) {
      return unpackTuple(entries);
    }

    Integer nextAnonymousID = 0;
    MappedArray<String, ExpressionEdge> valueEdges = new MappedArray<>();
    for (TupleValueEntryContext entryCtx : entries) {
      // each child has a value, and may have an identifier (named field)
      ExpressionVertex vxValue = entryCtx.expression().accept(this);
      String identifier;
      if (entryCtx.IDENTIFIER() != null) {
        identifier = entryCtx.IDENTIFIER().getText();
      } else {
        // TODO verify this against the specification
        identifier = nextAnonymousID.toString();
        nextAnonymousID += 1;
      }
      ExpressionEdge eValue = new ExpressionEdge(vxValue, null);
      valueEdges.put(identifier, eValue);
      exprGraph.addEdge(eValue);
    }
    TupleValueVertex vTuple = new TupleValueVertex(exprGraph, valueEdges);
    exprGraph.addVertex(vTuple);
    return vTuple;
  }

  private ExpressionVertex unpackTuple(List<TupleValueEntryContext> entries) {
    VariableIdentifier tmpTupleId = new VariableIdentifier(Arrays.asList("tmp_" + nextTmpVar));
    nextTmpVar += 1;
    ExpressionVertex tmpTuple = createVariableVertex(tmpTupleId);

    int entryNum = 0;
    boolean containsNamedEntry = false;
    for (TupleValueEntryContext entryCtx : entries) {
      ExpressionVertex entryVertex = entryCtx.expression().accept(this);

      if (!(entryVertex instanceof VariableReferenceVertex)) {
        String err = createLineError(entryCtx, "Unpacking target must be a variable");
        throw new FrontendBuildException(err);
      }

      ExpressionVertex attrVertex;
      TerminalNode entryId = entryCtx.IDENTIFIER();
      if (entryId != null) {

        attrVertex = createStaticAttributeAccessExpression(entryId.getText(), tmpTuple);
        containsNamedEntry = true;

      } else if (containsNamedEntry) {

        String err = createLineError(entryCtx, "Index-based entries must be unpacked before namespaced identifiers");
        throw new FrontendBuildException(err);

      } else {
        attrVertex = createStaticAttributeAccessExpression(entryNum, tmpTuple);
      }
      exprGraph.addEdge(new ExpressionEdge(attrVertex, entryVertex));

      entryNum += 1;
    }
    return tmpTuple;
  }

  @Override
  public ExpressionVertex visitFunctionTypeValue(
      FunctionTypeValueContext context) {
    // get the vertex corresponding to the input type
    ExpressionVertex vIn = context.tupleTypeValue(0).accept(this);
    ExpressionEdge eIn = new ExpressionEdge(vIn, null);
    // then get the output type vertex
    ExpressionVertex vOut = context.tupleTypeValue(1).accept(this);
    ExpressionEdge eOut = new ExpressionEdge(vOut, null);

    FunctionTypeValueVertex vFunctionType = new FunctionTypeValueVertex(
        exprGraph, eIn, eOut);
    exprGraph.addVertex(vFunctionType);
    exprGraph.addEdge(eIn);
    exprGraph.addEdge(eOut);
    return vFunctionType;
  }

  @Override
  public ExpressionVertex visitNamespacedIdentifier(
      NamespacedIdentifierContext context) {
    // keeping in mind that we may have constructed this variable already...
    VariableIdentifier id = getVariableIdentifier(context);
    ExpressionVertex v = createVariableVertex(id);
    if (v instanceof VariableReferenceVertex && isLHS) {
      ((VariableReferenceVertex) v).setExported(isPublic);
    }
    return v;
  }

  private ExpressionVertex createVariableVertex(VariableIdentifier id) {
    if (ReservedIdentifiers.getInstance()
        .isReservedIdentifier(id)) {
      // construct a constant value vertex with the identifier's value
      ConstantValueVertex vReserved = new ConstantValueVertex(exprGraph,
          ReservedIdentifiers.getInstance().getValue(id));
      exprGraph.addVertex(vReserved);
      return vReserved;
    } else {
      // this is a variable
      // TODO scope
      if (exprGraph.containsVariable(id)) {
        try {
          VariableReferenceVertex v = exprGraph.getVariableVertex(id);
          return v;
        } catch (VariableNotDefinedException e) {
          // cannot actually happen
          throw Throwables.propagate(e);
        }
      } else {
        try {
          exprGraph.addVertex(id);
          return exprGraph.getVariableVertex(id);
        } catch (MultipleDefinitionException e) {
          // cannot actually happen
          throw Throwables.propagate(e);
        }
      }
    }
  }

  @Override
  public ExpressionVertex visitImportExpr(ImportExprContext context) {
    String importString = context.STRING_VALUE().getText()
        .replaceAll("\\\"", "\"");
    importString = importString.substring(1, importString.length() - 1);

    // Reassign to a new variable to satisfy the lambda requirement of "final" variables
    String filePath = importString;

    File importedFile = Stream.of(
        new File(inputFile.getParent(), filePath),
        new File(inputFile.getParent(), filePath + ".manifold")
    ).filter(f -> f.exists())
        .findFirst()
        .orElseGet(() -> {
          File lib = getLib(filePath);
          if (lib == null) {
            lib = getLib(filePath + ".manifold");
          }
          if (lib != null) {
            return lib;
          }
          errors.add("Import " + filePath + " not found");
          return null;
        });
    if (importedFile == null) {
      return null;
    }

    ExpressionVertex v = new ImportVertex(exprGraph, importedFile);
    exprGraph.addVertex(v);
    return v;
  }

  @Override
  public ExpressionVertex visitTerminal(TerminalNode node) {
    if (node.getSymbol().getType() == ManifoldLexer.INTEGER_VALUE) {
      ConstantValueVertex v = new ConstantValueVertex(exprGraph,
          new IntegerValue(Integer.valueOf(node.getText())));
      exprGraph.addVertex(v);
      return v;
    } else if (node.getSymbol().getType() == ManifoldLexer.BOOLEAN_VALUE) {
      ConstantValueVertex v = new ConstantValueVertex(exprGraph,
          BooleanValue.getInstance(Boolean.parseBoolean(node.getText())));
      exprGraph.addVertex(v);
      return v;
    } else {
      throw new UndefinedBehaviourError(
          "unknown terminal node '" + node.getSymbol().getText() + "'");
    }
  }

  @Override
  public ExpressionVertex visitInfer(InferContext context) {
    ExpressionVertex v = new InferredValueVertex(exprGraph);
    exprGraph.addVertex(v);
    return v;
  }

  private VariableIdentifier getVariableIdentifier(NamespacedIdentifierContext context) {
    List<TerminalNode> identifierNodes = context.IDENTIFIER();
    List<String> identifierStrings = new LinkedList<>();
    for (TerminalNode node : identifierNodes) {
      identifierStrings.add(node.getText());
    }

    return new VariableIdentifier(identifierStrings);
  }

  @Override
  public StaticAttributeAccessVertex visitStaticAttributeAccessExpression(
      @NotNull StaticAttributeAccessExpressionContext ctx) {
    ExpressionVertex vRef = ctx.reference().accept(this);
    if (ctx.INTEGER_VALUE() != null) {
      return createStaticAttributeAccessExpression(Integer.parseInt(ctx.INTEGER_VALUE().toString()), vRef);
    }
    return createStaticAttributeAccessExpression(ctx.IDENTIFIER().getText(), vRef);
  }

  private StaticAttributeAccessVertex createStaticAttributeAccessExpression(int entryIdx, ExpressionVertex vRef) {
    ExpressionEdge e = new ExpressionEdge(vRef, null);
    exprGraph.addEdge(e);

    StaticAttributeAccessVertex attributeVertex = new StaticNumberAttributeAccessVertex(
        exprGraph, e, entryIdx);

    exprGraph.addVertex(attributeVertex);
    return attributeVertex;
  }

  private StaticAttributeAccessVertex createStaticAttributeAccessExpression(String attrName, ExpressionVertex vRef) {
    ExpressionEdge e = new ExpressionEdge(vRef, null);
    exprGraph.addEdge(e);

    StaticAttributeAccessVertex attributeVertex = new StaticStringAttributeAccessVertex(
        exprGraph, e, attrName);

    exprGraph.addVertex(attributeVertex);
    return attributeVertex;
  }

  private String createLineError(ParserRuleContext ctx, String reason) {
    Token start = ctx.getStart();
    StringBuilder sb = new StringBuilder()
        .append("Error at line ")
        .append(start.getLine())
        .append(", char ")
        .append(start.getCharPositionInLine() + 1)
        .append(": ")
        .append(reason);
    return sb.toString();
  }
}

