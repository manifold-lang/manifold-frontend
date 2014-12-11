package org.manifold.compiler.front;

import java.io.BufferedWriter;
import java.io.IOException;

import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

public class FunctionInvocationVertex extends ExpressionVertex {

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

}
