package org.manifold.compiler.front;

public class PrimitiveFunctionVertex extends ExpressionVertex {

  private PrimitiveFunctionValue function;
  
  public PrimitiveFunctionVertex(PrimitiveFunctionValue function) {
    this.function = function;
  }
  
  @Override
  public String toString() {
    return "primitive function " + function.getPrimitiveName();
  }
  
}
