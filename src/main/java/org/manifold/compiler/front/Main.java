package org.manifold.compiler.front;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.misc.Nullable;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.manifold.compiler.BooleanValue;
import org.manifold.compiler.ConnectionValue;
import org.manifold.compiler.Frontend;
import org.manifold.compiler.IntegerValue;
import org.manifold.compiler.NilTypeValue;
import org.manifold.compiler.NodeTypeValue;
import org.manifold.compiler.NodeValue;
import org.manifold.compiler.PortTypeValue;
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
import org.manifold.parser.ManifoldParser.RValueExpressionContext;

import com.google.common.base.Throwables;

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
    int step = 1;
    // now proceed
    while (!(funcalls.isEmpty())) {
      // get next vertex
      Iterator<FunctionInvocationVertex> iterator = funcalls.iterator();
      FunctionInvocationVertex v = iterator.next();
      funcalls.remove(v);
      log.debug("elaborating function "
          + Integer.toString(System.identityHashCode(v)));
      v.elaborate();
      log.debug("writing out expression graph at function elaboration step " +
          step);
      File elaboratedDot = new File("tmp.elaborated.step" + step + ".dot");
      g.writeDOTFile(elaboratedDot);
      step++;
      // TODO it would be more efficient for the vertex to tell us whether
      // any new function invocations were created during elaboration
      for (ExpressionVertex vNew : g.getNonVariableVertices()) {
        if (vNew instanceof FunctionInvocationVertex) {
          funcalls.add((FunctionInvocationVertex) vNew);
        }
      }
    }
  }

  public static void elaborateSchematicTypes(ExpressionGraph g, Schematic s)
      throws Exception {
    // look for all primitive node/port vertices in the expression graph;
    // for each one, find any variables to which
    // it is directly assigned, and use the non-namespaced identifier
    // as the type name in the schematic
    for (ExpressionVertex v : g.getNonVariableVertices()) {
      if (v instanceof PrimitivePortVertex ||
          v instanceof PrimitiveNodeVertex) {
        v.elaborate(); // usually redundant but always safe
        List<ExpressionEdge> outgoingEdges = g.getEdgesFromSource(v);
        for (ExpressionEdge e : outgoingEdges) {
          if (e.getTarget() instanceof VariableReferenceVertex) {
            VariableReferenceVertex id = (VariableReferenceVertex)
                e.getTarget();
            String typename = id.getIdentifier().getName();
            // now add to the correct schematic type list
            if (v instanceof PrimitivePortVertex) {
              log.debug("elaborated port type " + typename);
              PortTypeValue portType = (PortTypeValue) v.getValue();
              s.addPortType(typename, portType);
            } else if (v instanceof PrimitiveNodeVertex) {
              log.debug("elaborated node type " + typename);
              NodeTypeValue nodeType = (NodeTypeValue) v.getValue();
              s.addNodeType(typename, nodeType);
            }
          }
        } // for (e: outgoingEdges)
      } // if (v instanceof ...)
    }
  }

  public static void elaborateNodes(ExpressionGraph g, Schematic s)
      throws Exception {
    // In the first pass, elaborate every NodeValueVertex
    // in the expression graph. In the second pass, connect the inputs of
    // every NodeValueVertex, then add to the schematic
    // each node and each connection that was created this way.

    log.debug("elaborating nodes");

    List<NodeValueVertex> nodeVertices = new LinkedList<>();
    for (ExpressionVertex v : g.getNonVariableVertices()) {
      if (v instanceof NodeValueVertex) {
        nodeVertices.add((NodeValueVertex) v);
      }
    }

    // pass 1
    log.debug("pass 1");
    for (NodeValueVertex nv : nodeVertices) {
      log.debug("elaborating node "
          + Integer.toString(System.identityHashCode(nv)));
      nv.elaborate();
    }
    // pass 2
    log.debug("pass 2");
    List<ConnectionValue> connections = new LinkedList<>();
    List<NodeValue> nodes = new LinkedList<>();
    for (NodeValueVertex nv : nodeVertices) {
      log.debug("connecting node "
          + Integer.toString(System.identityHashCode(nv)));
      List<ConnectionValue> cs = nv.connect();
      if (cs.size() == 1) {
        log.debug("1 connection made");
      } else {
        log.debug(Integer.toString(cs.size()) + " connections made");
      }
      connections.addAll(cs);
      nodes.add(nv.getNodeValue());
    }
    // build schematic
    Integer nodeID = 1;
    for (NodeValue node : nodes) {
      String nodeName = "n" + nodeID.toString();
      s.addNode(nodeName, node);
      nodeID += 1;
    }
    Integer connectionID = 1;
    for (ConnectionValue conn : connections) {
      String connName = "c" + connectionID.toString();
      s.addConnection(connName, conn);
      connectionID += 1;
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

    StringBuilder errors = new StringBuilder();
    parser.addErrorListener(new ANTLRErrorListener() {

      @Override
      public void syntaxError(@NotNull Recognizer<?, ?> recognizer, @Nullable Object offendingSymbol, int line,
                              int charPositionInLine, @NotNull String msg, @Nullable RecognitionException e) {
        errors.append("Error at line ").append(line).append(", char ")
                .append(charPositionInLine).append(": ").append(msg).append("\n");
      }

      @Override
      public void reportAmbiguity(@NotNull Parser recognizer, @NotNull DFA dfa, int startIndex, int stopIndex,
                                  boolean exact, @Nullable BitSet ambigAlts, @NotNull ATNConfigSet configs) {
        // Pass
      }

      @Override
      public void reportAttemptingFullContext(@NotNull Parser recognizer, @NotNull DFA dfa, int startIndex,
                                              int stopIndex, @Nullable BitSet conflictingAlts,
                                              @NotNull ATNConfigSet configs) {
        // Pass
      }

      @Override
      public void reportContextSensitivity(@NotNull Parser recognizer, @NotNull DFA dfa, int startIndex, int stopIndex,
                                           int prediction, @NotNull ATNConfigSet configs) {
        // Pass
      }
    });

    // Specify our entry point
    ManifoldParser.SchematicContext context = parser.schematic();

    if (errors.length() != 0) {
      throw new FrontendBuildException(errors.toString());
    }

    ExpressionContextVisitor graphBuilder = new ExpressionContextVisitor();
    List<ExpressionContext> expressionContexts = context.expression();
    for (ExpressionContext expressionContext : expressionContexts) {
      graphBuilder.visit(expressionContext);
    }
    ExpressionGraph exprGraph = graphBuilder.getExpressionGraph();
    log.debug("writing out initial expression graph");
    File exprGraphDot = new File(inputFile.getName() + ".exprs.dot");
    exprGraph.writeDOTFile(exprGraphDot);

    exprGraph.verifyVariablesSingleAssignment();

    Schematic schematic = new Schematic(inputFile.getName());

    elaborateFunctions(exprGraph);
    log.debug("writing out expression graph after function elaboration");
    File elaboratedDot = new File(inputFile.getName() + ".elaborated.dot");
    exprGraph.writeDOTFile(elaboratedDot);

    elaborateSchematicTypes(exprGraph, schematic);
    elaborateNodes(exprGraph, schematic);

    return schematic;

  }
}

class ExpressionContextVisitor extends ManifoldBaseVisitor<ExpressionVertex> {

  private ExpressionGraph exprGraph;
  public ExpressionGraph getExpressionGraph() {
    return this.exprGraph;
  }

  public ExpressionContextVisitor() {
    this.exprGraph = new ExpressionGraph();
  }

  @Override
  public ExpressionVertex visitAssignmentExpression(
      ManifoldParser.AssignmentExpressionContext context) {
    // get the vertex corresponding to the lvalue
    ExpressionVertex vLeft = context.lvalue().accept(this);
    // then get the rvalue...
    ExpressionVertex vRight = context.rvalue().accept(this);

    ExpressionEdge e = new ExpressionEdge(vRight, vLeft);
    exprGraph.addEdge(e);
    return vRight;
  }

  @Override
  public ExpressionVertex visitFunctionInvocationExpression(
      ManifoldParser.FunctionInvocationExpressionContext context) {
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
      ManifoldParser.FunctionValueContext ctx) {
    ExpressionContextVisitor functionGraphBuilder =
        new ExpressionContextVisitor();
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
      ManifoldParser.PrimitiveNodeDefinitionExpressionContext context) {
    ExpressionVertex vSignature = context.functionTypeValue().accept(this);
    ExpressionEdge eSignature = new ExpressionEdge(vSignature, null);
    exprGraph.addEdge(eSignature);
    PrimitiveNodeVertex vNode = new PrimitiveNodeVertex(exprGraph, eSignature);
    exprGraph.addVertex(vNode);
    return vNode;
  }

  @Override
  public ExpressionVertex visitPrimitivePortDefinitionExpression(
      ManifoldParser.PrimitivePortDefinitionExpressionContext context) {

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
    Map<String, ExpressionEdge> typeValueEdges = new HashMap<>();
    Map<String, ExpressionEdge> defaultValueEdges = new HashMap<>();
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
    LinkedHashMap<String, ExpressionEdge> valueEdges = new LinkedHashMap<>();
    Integer nextAnonymousID = 0;
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
        // doesn't exist yet
        try {
          exprGraph.addVertex(id);
        } catch (MultipleDefinitionException e2) {
          System.err.println("multiple definitions of variable " + id);
          throw new ParseCancellationException();
        }
        try {
          VariableReferenceVertex v = exprGraph.getVariableVertex(id);
          return v;
        } catch (VariableNotDefinedException e2) {
          throw new UndefinedBehaviourError("failed to define variable "
              + id);
        }
      }
    }
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
          @NotNull ManifoldParser.StaticAttributeAccessExpressionContext ctx) {
    ExpressionVertex vRef = ctx.reference().accept(this);
    ExpressionEdge e = new ExpressionEdge(vRef, null);
    exprGraph.addEdge(e);

    if (ctx.INTEGER_VALUE() != null) {
      return new StaticNumberAttributeAccessVertex(exprGraph, e, Integer.parseInt(ctx.INTEGER_VALUE().toString()));
    }

    return new StaticStringAttributeAccessVertex(exprGraph, e, ctx.IDENTIFIER().toString());
  }

}
