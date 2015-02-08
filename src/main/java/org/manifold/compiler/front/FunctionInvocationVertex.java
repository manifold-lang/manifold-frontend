package org.manifold.compiler.front;

import java.io.BufferedWriter;
import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.manifold.compiler.NodeTypeValue;
import org.manifold.compiler.TypeValue;
import org.manifold.compiler.UndefinedBehaviourError;
import org.manifold.compiler.Value;

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
  }
  
  @Override
  public void elaborate() throws Exception {
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
    } else {
      throw new UndefinedBehaviourError("don't know how to invoke '"
          + function.toString() + "'");
    }
  }

}
