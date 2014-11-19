package org.manifold.compiler.front;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.manifold.compiler.ConnectionType;
import org.manifold.compiler.Frontend;
import org.manifold.compiler.IntegerValue;
import org.manifold.compiler.NilTypeValue;
import org.manifold.compiler.NodeTypeValue;
import org.manifold.compiler.PortTypeValue;
import org.manifold.compiler.TypeValue;
import org.manifold.compiler.UndeclaredIdentifierException;
import org.manifold.compiler.UndefinedBehaviourError;
import org.manifold.compiler.Value;
import org.manifold.compiler.middle.Schematic;
import org.manifold.parser.ManifoldBaseVisitor;
import org.manifold.parser.ManifoldLexer;
import org.manifold.parser.ManifoldParser;
import org.manifold.parser.ManifoldParser.ExpressionContext;
import org.manifold.parser.ManifoldParser.NamespacedIdentifierContext;

import com.google.common.annotations.VisibleForTesting;

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

    System.out.println("expressions:");
    System.out.print(expressions);
    System.out.println();

    NamespaceIdentifier defaultNamespaceID = new NamespaceIdentifier("");
    Namespace defaultNamespace = new Namespace(defaultNamespaceID);
    // "top-level" bindings go into the private scope of the default namespace

    Schematic schematic = new Schematic(inputFile.getName());

    // mock-up: digital circuits primitives
    // (to be removed when core library and namespaces are implemented)
    createDigitalPrimitives(defaultNamespace.getPrivateScope(), schematic);

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

    System.out.println("top-level identifiers:");
    for (VariableIdentifier id :
        defaultNamespace.getPrivateScope().getSymbolIdentifiers()) {
      System.out.println(id);
    }

    // TODO this should take a namespace map instead
    ExpressionGraph exprGraph = new ExpressionGraph(
        defaultNamespace.getPrivateScope());
    exprGraph.buildFrom(expressions);
    exprGraph.removeUnconnectedEdges();
    exprGraph.optimizeOutVariables();

    System.out.println("expression graph edges:");
    for (String s : exprGraph.getPrintableEdges()) {
      System.out.println(s);
    }

    exprGraph.elaboratePrimitives();

    System.out.println("instantiated primitives:");
    for (String s : exprGraph.getPrintableInstances()) {
      System.out.println(s);
    }

    exprGraph.elaborateConnections(schematic);

    exprGraph.writeSchematic(schematic);

    return schematic;
  }

  private static void setupDigitalTypes(Schematic s)
      throws org.manifold.compiler.MultipleDefinitionException,
      UndeclaredIdentifierException {
    PortTypeValue digitalInPortType;
    PortTypeValue digitalOutPortType;

    Map<String, TypeValue> noTypeAttributes = new HashMap<>();
    Map<String, Value> noAttributes = new HashMap<>();

    Map<String, TypeValue> registerTypeAttributes = new HashMap<>();
    Map<String, PortTypeValue> registerTypePorts = new HashMap<>();
    NodeTypeValue registerType;

    Map<String, PortTypeValue> andTypePorts = new HashMap<>();
    NodeTypeValue andType;

    Map<String, PortTypeValue> orTypePorts = new HashMap<>();
    NodeTypeValue orType;

    Map<String, PortTypeValue> notTypePorts = new HashMap<>();
    NodeTypeValue notType;

    Map<String, PortTypeValue> inputPinTypePorts = new HashMap<>();
    NodeTypeValue inputPinType;

    Map<String, PortTypeValue> outputPinTypePorts = new HashMap<>();
    NodeTypeValue outputPinType;

    ConnectionType digitalWireType;

    digitalInPortType = new PortTypeValue(s.getUserDefinedType("Bool"),
        noTypeAttributes);
    digitalOutPortType = new PortTypeValue(s.getUserDefinedType("Bool"),
        noTypeAttributes);

    registerTypeAttributes.put("initialValue", BooleanTypeValue.getInstance());
    registerTypeAttributes.put("resetActiveHigh",
        BooleanTypeValue.getInstance());
    registerTypeAttributes.put("resetAsynchronous",
        BooleanTypeValue.getInstance());
    registerTypeAttributes.put("clockActiveHigh",
        BooleanTypeValue.getInstance());
    registerTypePorts.put("in", digitalInPortType);
    registerTypePorts.put("out", digitalOutPortType);
    registerTypePorts.put("clock", digitalInPortType);
    registerTypePorts.put("reset", digitalInPortType);
    registerType = new NodeTypeValue(registerTypeAttributes, registerTypePorts);

    andTypePorts.put("in0", digitalInPortType);
    andTypePorts.put("in1", digitalInPortType);
    andTypePorts.put("out", digitalOutPortType);
    andType = new NodeTypeValue(noTypeAttributes, andTypePorts);

    orTypePorts.put("in0", digitalInPortType);
    orTypePorts.put("in1", digitalInPortType);
    orTypePorts.put("out", digitalOutPortType);
    orType = new NodeTypeValue(noTypeAttributes, orTypePorts);

    notTypePorts.put("in", digitalInPortType);
    notTypePorts.put("out", digitalOutPortType);
    notType = new NodeTypeValue(noTypeAttributes, notTypePorts);

    inputPinTypePorts.put("out", digitalOutPortType);
    inputPinType = new NodeTypeValue(noTypeAttributes, inputPinTypePorts);

    outputPinTypePorts.put("in", digitalInPortType);
    outputPinType = new NodeTypeValue(noTypeAttributes, outputPinTypePorts);

    digitalWireType = new ConnectionType(noTypeAttributes);

    s.addPortType("digitalIn", digitalInPortType);
    s.addPortType("digitalOut", digitalOutPortType);

    s.addNodeType("register", registerType);
    s.addNodeType("and", andType);
    s.addNodeType("or", orType);
    s.addNodeType("not", notType);
    s.addNodeType("inputPin", inputPinType);
    s.addNodeType("outputPin", outputPinType);

    s.addConnectionType("digitalWire", digitalWireType);

  }

  @VisibleForTesting
  public static void createDigitalPrimitives(Scope scope, Schematic schematic)
      throws MultipleDefinitionException, VariableNotDefinedException,
      MultipleAssignmentException, UndeclaredIdentifierException,
      org.manifold.compiler.MultipleDefinitionException {
    setupDigitalTypes(schematic);
    // inputPin: unit -> Bool
    FunctionTypeValue inputPinPrimitiveType = new FunctionTypeValue(
        NilTypeValue.getInstance(), BooleanTypeValue.getInstance());
    PrimitiveFunctionValue inputPinPrimitive = new PrimitiveFunctionValue(
        "inputPin", inputPinPrimitiveType, schematic.getNodeType("inputPin"));
    VariableIdentifier inputPinIdentifier = new VariableIdentifier(
        Arrays.asList(new String[]{"inputPin"}));
    scope.defineVariable(inputPinIdentifier);
    scope.assignVariable(inputPinIdentifier,
        new LiteralExpression(inputPinPrimitive));
    // outputPin: Bool -> unit
    FunctionTypeValue outputPinPrimitiveType = new FunctionTypeValue(
        BooleanTypeValue.getInstance(), NilTypeValue.getInstance());
    PrimitiveFunctionValue outputPinPrimitive = new PrimitiveFunctionValue(
        "outputPin", outputPinPrimitiveType,
        schematic.getNodeType("outputPin"));
    VariableIdentifier outputPinIdentifier = new VariableIdentifier(
        Arrays.asList(new String[]{"outputPin"}));
    scope.defineVariable(outputPinIdentifier);
    scope.assignVariable(outputPinIdentifier,
        new LiteralExpression(outputPinPrimitive));
    // and: (Bool, Bool) -> Bool
    FunctionTypeValue andPrimitiveType = new FunctionTypeValue(
        new TupleTypeValue(Arrays.asList(new TypeValue[]{
            BooleanTypeValue.getInstance(), BooleanTypeValue.getInstance()
        })), BooleanTypeValue.getInstance());
    PrimitiveFunctionValue andPrimitive = new PrimitiveFunctionValue(
        "and", andPrimitiveType, schematic.getNodeType("and"));
    VariableIdentifier andIdentifier = new VariableIdentifier(
        Arrays.asList(new String[]{"and"}));
    scope.defineVariable(andIdentifier);
    scope.assignVariable(andIdentifier, new LiteralExpression(andPrimitive));
    // or: (Bool, Bool) -> Bool
    FunctionTypeValue orPrimitiveType = new FunctionTypeValue(
        new TupleTypeValue(Arrays.asList(new TypeValue[]{
            BooleanTypeValue.getInstance(), BooleanTypeValue.getInstance()
        })), BooleanTypeValue.getInstance());
    PrimitiveFunctionValue orPrimitive = new PrimitiveFunctionValue(
        "or", orPrimitiveType, schematic.getNodeType("or"));
    VariableIdentifier orIdentifier = new VariableIdentifier(
        Arrays.asList(new String[]{"or"}));
    scope.defineVariable(orIdentifier);
    scope.assignVariable(orIdentifier, new LiteralExpression(orPrimitive));
    // not: Bool -> Bool
    FunctionTypeValue notPrimitiveType = new FunctionTypeValue(
        BooleanTypeValue.getInstance(), BooleanTypeValue.getInstance());
    PrimitiveFunctionValue notPrimitive = new PrimitiveFunctionValue(
        "not", notPrimitiveType, schematic.getNodeType("not"));
    VariableIdentifier notIdentifier = new VariableIdentifier(
        Arrays.asList(new String[]{"not"}));
    scope.defineVariable(notIdentifier);
    scope.assignVariable(notIdentifier, new LiteralExpression(notPrimitive));
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
  public Expression visitTupleValue(ManifoldParser.TupleValueContext context) {
    // get the expressions resulting from visiting all value entries
    List<Expression> values = new ArrayList<Expression>();
    for (ManifoldParser.TupleValueEntryContext subctx
        : context.tupleValueEntry()) {
      values.add(visit(subctx.expression()));
    }
    // construct a type
    Scope emptyScope = new Scope();
    List<TypeValue> types = new ArrayList<TypeValue>();
    for (Expression e : values) {
      types.add(e.getType(emptyScope));
    }
    TupleTypeValue anonymousTupleType = new TupleTypeValue(types);
    // now we build a TupleValue from these subexpressions
    return new LiteralExpression(new TupleValue(anonymousTupleType, values));
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
          "unknown terminal node type " + node.getSymbol().getType());
    }
  }

}
