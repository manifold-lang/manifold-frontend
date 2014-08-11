package org.manifold.compiler.front;

public class VariableReferenceVertex extends ExpressionVertex {
  private VariableIdentifier id;
  public VariableIdentifier getIdentifier() {
    return id;
  }
  
  public VariableReferenceVertex(VariableIdentifier id) {
    this.id = id;
  }
  
  @Override
  public String toString() {
    return "var " + id.toString();
  }
}
