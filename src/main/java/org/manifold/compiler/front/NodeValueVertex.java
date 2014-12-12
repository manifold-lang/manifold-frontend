package org.manifold.compiler.front;

import java.io.BufferedWriter;
import java.io.IOException;

import org.manifold.compiler.NodeTypeValue;
import org.manifold.compiler.NodeValue;
import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

public class NodeValueVertex extends ExpressionVertex {

  private final NodeTypeValue nodeType;
  @Override
  public TypeValue getType() {
    return nodeType;
  }
  
  private NodeValue node;
  @Override
  public Value getValue() {
    return node;
  }
  
  private ExpressionEdge inputEdge;
  
  public NodeValueVertex(ExpressionGraph exprGraph,
      NodeTypeValue nodeType, ExpressionEdge inputEdge) {
    super(exprGraph);
    this.nodeType = nodeType;
    this.inputEdge = inputEdge;
    this.inputEdge.setTarget(this);
    this.inputEdge.setName("input");
  }

  @Override
  public void elaborate() throws Exception {
    // TODO Auto-generated method stub
    
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
  public String toString() {
    if (node == null) {
      return "node (not elaborated)";
    } else {
      return "node (" + node.toString() + ")";
    }
  }
  
  @Override
  public void writeToDOTFile(BufferedWriter writer) throws IOException {
    String objectID = Integer.toString(System.identityHashCode(this));
    String label = this.toString();
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

}
