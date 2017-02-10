package org.manifold.compiler.front;

import com.google.common.base.Preconditions;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.manifold.compiler.NodeTypeValue;
import org.manifold.compiler.TypeValue;
import org.manifold.compiler.UndefinedBehaviourError;
import org.manifold.compiler.Value;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

public class FunctionInvocationVertex extends ExpressionVertex {

  private static Logger log = LogManager.getLogger("FunctionInvocationVertex");

  private ExpressionEdge functionEdge;
  public ExpressionEdge getFunctionEdge() {
    return functionEdge;
  }

  private ExpressionEdge inputEdge;
  public ExpressionEdge getInputEdge() {
    return inputEdge;
  }

  public FunctionInvocationVertex(ExpressionGraph exprGraph,
      ExpressionEdge functionEdge, ExpressionEdge inputEdge) {
    super(exprGraph);
    this.functionEdge = functionEdge;
    this.functionEdge.setTarget(this);
    this.functionEdge.setName("function");
    this.inputEdge = inputEdge;
    this.inputEdge.setTarget(this);
    this.inputEdge.setName("argument");
  }

  @Override
  public TypeValue getType() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Value getValue() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void verify() throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean isElaborationtimeKnowable() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isRuntimeKnowable() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void writeToDOTFile(BufferedWriter writer) throws IOException {
    String objectID = Integer.toString(System.identityHashCode(this));
    String label = "invoke function";
    writer.write(objectID);
    writer.write(" [");
    writer.write("label=\"");
    writer.write(objectID);
    writer.write("\n");
    writer.write(label);
    writer.write("\"");
    writer.write("];");
    writer.newLine();
  }

  @Override
  public ExpressionVertex copy(ExpressionGraph g, Map<ExpressionEdge, ExpressionEdge> edgeMap) {
    Preconditions.checkArgument(edgeMap.containsKey(functionEdge) && edgeMap.containsKey(inputEdge));
    return new FunctionInvocationVertex(g, edgeMap.get(functionEdge), edgeMap.get(inputEdge));
  }

  private void elaborateNodeInstantiation(Value function,
      ExpressionVertex vFunction) throws Exception {
    log.debug("function invocation is node instantiation");
    NodeTypeValue nodeType = (NodeTypeValue) function;
    FunctionTypeValue signature = (FunctionTypeValue) vFunction.getType();
    NodeValueVertex vNode = new NodeValueVertex(getExpressionGraph(),
        nodeType, signature, inputEdge);
    // now inputEdge.target is vNode
    getExpressionGraph().addVertex(vNode);
    // change source of all edges out from this vertex
    // to have vNode as their source
    for (ExpressionEdge e : getExpressionGraph().getEdgesFromSource(this)) {
      e.setSource(vNode);
    }
    // destroy the function edge
    getExpressionGraph().removeEdge(functionEdge);
    // now remove this vertex from the graph
    getExpressionGraph().removeVertex(this);

    // Now actually elaborate the new node, since if this was a non-primitive
    // it would be resolved and that's what consumers of this API expect
    vNode.elaborate();
  }

  private void elaborateNonPrimitiveFunction(Value f,
      ExpressionVertex input) throws Exception {
    log.debug("function invocation is non-primitive elaboration");
    FunctionValue function = (FunctionValue) f;
    FunctionTypeValue signature = (FunctionTypeValue) f.getType();
    TupleTypeValue inputType = (TupleTypeValue) signature.getInputType();
    TupleTypeValue outputType = (TupleTypeValue) signature.getOutputType();
    // calculate renaming map (body -> this.exprGraph)
    Map<VariableReferenceVertex, VariableReferenceVertex> renamingMap =
        new HashMap<>();
    for (Entry<VariableIdentifier, VariableReferenceVertex> entry
        : function.getBody().getVariableVertices().entrySet()) {
      VariableReferenceVertex varTarget;
      if (getExpressionGraph().containsVariable(entry.getKey())) {
        // if the main graph contains this variable, don't rename; use existing
        log.debug("preserving variable " + entry.getKey());
        varTarget = getExpressionGraph().getVariableVertex(entry.getKey());
      } else {
        // otherwise, create a fresh variable in the main graph and map to that
        String oldName = entry.getKey().getName();
        Random rng = new Random();
        while (true) {
          // choose a random suffix and mangle
          Integer i = rng.nextInt(Integer.MAX_VALUE);
          String newName = oldName + "_" + i.toString();
          List<String> newNames = new ArrayList<>();
          newNames.add(newName);
          VariableIdentifier newID = new VariableIdentifier(newNames);
          if (!getExpressionGraph().containsVariable(newID)) {
            log.debug("renaming local variable " + entry.getKey()
                + " to " + newID);
            getExpressionGraph().addVertex(newID);
            varTarget = getExpressionGraph().getVariableVertex(newID);
            break;
          }
        }
      }
      renamingMap.put(entry.getValue(), varTarget);
    }
    // identify main graph input and output edges
    // we assume there's exactly one of each
    ExpressionEdge mainGraphInput = inputEdge;
    log.debug("main graph input is " + mainGraphInput);
    ExpressionEdge mainGraphOutput = null;

    List<ExpressionEdge> sourceEdges = getExpressionGraph().getEdgesFromSource(this);
    if (sourceEdges.size() > 0) {
      mainGraphOutput = sourceEdges.get(0);
    }

    log.debug("main graph output is " + mainGraphOutput);
    // identify subgraph (body) input and output vertices
    TupleValueVertex subGraphInput = function.getInputVertex();
    TupleValueVertex subGraphOutput = function.getOutputVertex();

    // perform copy
    getExpressionGraph().addFunctionExpressionGraph(function.getBody(),
        mainGraphInput, subGraphInput, mainGraphOutput, subGraphOutput,
        renamingMap);
    // destroy the function edge
    getExpressionGraph().removeEdge(functionEdge);
    // now remove this vertex from the graph
    getExpressionGraph().removeVertex(this);
  }

  private boolean elaborated = false;

  @Override
  public void elaborate() throws Exception {
    if (elaborated) {
      return;
    }
    // Elaborate argument
    ExpressionVertex vInput = inputEdge.getSource();
    vInput.elaborate();
    // Elaborate function
    ExpressionVertex vFunction = functionEdge.getSource();
    vFunction.elaborate();
    // now find out what kind of function we are about to invoke
    Value function = vFunction.getValue();
    if (function instanceof NodeTypeValue) {
      // we're not calling a function; we're instantiating a node!
      elaborateNodeInstantiation(function, vFunction);
    } else if (function instanceof FunctionValue) {
      // non-primitive function elaboration
      elaborateNonPrimitiveFunction(function, vInput);
    } else {
      throw new UndefinedBehaviourError("don't know how to invoke '"
          + function.toString() + "'");
    }
    elaborated = true;
  }

}
