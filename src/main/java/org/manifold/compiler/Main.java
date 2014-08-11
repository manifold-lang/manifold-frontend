package org.manifold.compiler;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.manifold.compiler.back.digital.VHDLCodeGenerator;
import org.manifold.compiler.front.Expression;
import org.manifold.compiler.front.ExpressionGraph;
import org.manifold.compiler.front.FunctionInvocationExpression;
import org.manifold.compiler.front.FunctionTypeValue;
import org.manifold.compiler.front.LiteralExpression;
import org.manifold.compiler.front.MultipleAssignmentException;
import org.manifold.compiler.front.MultipleDefinitionException;
import org.manifold.compiler.front.PrimitiveFunctionValue;
import org.manifold.compiler.front.Scope;
import org.manifold.compiler.front.TupleTypeValue;
import org.manifold.compiler.front.TupleValue;
import org.manifold.compiler.front.VariableAssignmentExpression;
import org.manifold.compiler.front.VariableIdentifier;
import org.manifold.compiler.front.VariableNotDefinedException;
import org.manifold.compiler.front.VariableReferenceExpression;
import org.manifold.compiler.middle.Schematic;
import org.manifold.compiler.middle.serialization.SchematicSerializer;
import org.manifold.parser.ManifoldBaseVisitor;
import org.manifold.parser.ManifoldLexer;
import org.manifold.parser.ManifoldParser;
import org.manifold.parser.ManifoldParser.ExpressionContext;
import org.manifold.parser.ManifoldParser.NamespacedIdentifierContext;

import com.google.common.annotations.VisibleForTesting;

public class Main {

  public static void main(String[] args) throws Exception {

    ManifoldLexer lexer = new ManifoldLexer(new ANTLRInputStream(
        new FileInputStream(args[0])));

     // Get a list of matched tokens
    CommonTokenStream tokens = new CommonTokenStream(lexer);

    // Pass the tokens to the parser
    ManifoldParser parser = new ManifoldParser(tokens);

    // Specify our entry point
    ManifoldParser.SchematicContext context = parser.schematic();
    
    ExpressionVisitor visitor = new ExpressionVisitor();

    List<Expression> expressions = new LinkedList<>();
    List<ExpressionContext> expressionContexts = context.expression();
    
    for (ExpressionContext expressionContext : expressionContexts) {
      expressions.add(visitor.visit(expressionContext));
    }
    
    System.out.println("expressions:");
    System.out.print(expressions);
    System.out.println();
    
    Scope toplevel = new Scope();
    Schematic schematic = new Schematic(args[0]);
    
    // mock-up: digital circuits primitives
    // (to be removed when core library and namespaces are implemented)
    createDigitalPrimitives(toplevel, schematic);
    
    // Build top-level scope
    for (Expression expr : expressions) {
      if (expr instanceof VariableAssignmentExpression) {
        VariableAssignmentExpression assign = 
            (VariableAssignmentExpression) expr;
        Expression lvalue = assign.getLvalueExpression();
        Expression rvalue = assign.getRvalueExpression();
        
        // we expect the lvalue to be a variable reference
        if (lvalue instanceof VariableReferenceExpression) {
          VariableReferenceExpression vRef = 
              (VariableReferenceExpression) lvalue;
          VariableIdentifier identifier = vRef.getIdentifier();
          Expression idType = new LiteralExpression(rvalue.getType(toplevel)); 
          toplevel.defineVariable(identifier, idType);
          toplevel.assignVariable(identifier, rvalue);
        } else {
          throw new UndefinedBehaviourError(
              "unhandled lvalue type" + lvalue.getClass());
        }
      }
    }
    
    System.out.println("top-level identifiers:");
    for (VariableIdentifier id : toplevel.getSymbolIdentifiers()) {
      System.out.println(id);
    }
   
    ExpressionGraph exprGraph = new ExpressionGraph(toplevel);
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
    
    SchematicSerializer ser = new SchematicSerializer();
    BufferedWriter consoleWriter = new BufferedWriter(
        new OutputStreamWriter(System.out));
    ser.serialize(schematic, consoleWriter);
    consoleWriter.flush();
    
    VHDLCodeGenerator codegen = new VHDLCodeGenerator(schematic);
    codegen.generateOutputProducts();
  }
  
  private static void setupDigitalTypes(Schematic s) 
      throws org.manifold.compiler.MultipleDefinitionException {
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
    
    digitalInPortType = new PortTypeValue(noTypeAttributes);
    digitalOutPortType = new PortTypeValue(noTypeAttributes);

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
    scope.defineVariable(inputPinIdentifier, 
        new LiteralExpression(inputPinPrimitiveType));
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
    scope.defineVariable(outputPinIdentifier, 
        new LiteralExpression(outputPinPrimitiveType));
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
    scope.defineVariable(andIdentifier, 
        new LiteralExpression(andPrimitiveType));
    scope.assignVariable(andIdentifier, new LiteralExpression(andPrimitive));
    // or: (Bool, Bool) -> Bool
    FunctionTypeValue orPrimitiveType = new FunctionTypeValue(
        new TupleTypeValue(Arrays.asList(new TypeValue[]{
            BooleanTypeValue.getInstance(), BooleanTypeValue.getInstance()
        })), BooleanTypeValue.getInstance());
    PrimitiveFunctionValue orPrimitive = new PrimitiveFunctionValue(
        "or", orPrimitiveType, schematic.getNodeType("inputPin"));
    VariableIdentifier orIdentifier = new VariableIdentifier(
        Arrays.asList(new String[]{"or"}));
    scope.defineVariable(orIdentifier, 
        new LiteralExpression(orPrimitiveType));
    scope.assignVariable(orIdentifier, new LiteralExpression(orPrimitive));
    // not: Bool -> Bool
    FunctionTypeValue notPrimitiveType = new FunctionTypeValue(
        BooleanTypeValue.getInstance(), BooleanTypeValue.getInstance());
    PrimitiveFunctionValue notPrimitive = new PrimitiveFunctionValue(
        "not", notPrimitiveType, schematic.getNodeType("inputPin"));
    VariableIdentifier notIdentifier = new VariableIdentifier(
        Arrays.asList(new String[]{"not"}));
    scope.defineVariable(notIdentifier, 
        new LiteralExpression(notPrimitiveType));
    scope.assignVariable(notIdentifier, new LiteralExpression(notPrimitive));
  }
  
}

class ExpressionVisitor extends ManifoldBaseVisitor<Expression> {
  
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
