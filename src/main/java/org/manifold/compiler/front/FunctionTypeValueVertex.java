package org.manifold.compiler.front;

import java.io.BufferedWriter;
import java.io.IOException;

import org.manifold.compiler.TypeTypeValue;
import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

public class FunctionTypeValueVertex extends ExpressionVertex {

  private final FunctionTypeValueExpression typeExpr;
  public FunctionTypeValueExpression getTypeExpression() {
    return typeExpr;
  }
  
  private FunctionTypeValue type = null;
  @Override
  public Value getValue() {
    return this.type;
  }
  
  private ExpressionEdge inputTypeEdge;
  public ExpressionEdge getInputTypeEdge() {
    return inputTypeEdge;
  }

  private ExpressionEdge outputTypeEdge;
  public ExpressionEdge getOutputTypeEdge() {
    return outputTypeEdge;
  }
  
  public FunctionTypeValueVertex(ExpressionGraph g,
      FunctionTypeValueExpression typeExpr,
      ExpressionEdge inputTypeEdge, ExpressionEdge outputTypeEdge) {
    super(g);
    this.typeExpr = typeExpr;
    this.inputTypeEdge = inputTypeEdge;
    this.inputTypeEdge.setTarget(this);
    this.inputTypeEdge.setName("input type");
    this.outputTypeEdge = outputTypeEdge;
    this.outputTypeEdge.setTarget(this);
    this.outputTypeEdge.setName("output type");
  }
  
  @Override
  public String toString() {
    if (type == null) {
      return "function type value (not elaborated)";
    } else {
      return "function type value (" + type.toString() + ")";
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

  @Override
  public TypeValue getType() {
    return TypeTypeValue.getInstance();
  }

  @Override
  public void verify() throws Exception {
    // TODO Auto-generated method stub
    
  }

  @Override
  public boolean isElaborationtimeKnowable() {
    return true;
  }

  @Override
  public boolean isRuntimeKnowable() {
    return false;
  }
  
}
