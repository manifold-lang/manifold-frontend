package org.manifold.compiler.front;

import java.util.ArrayList;

import org.manifold.compiler.ValueVisitor;


public class PrimitiveFunctionValue extends FunctionValue {

  private final String primitiveName;
  public String getPrimitiveName() {
    return primitiveName;
  }
  
  public PrimitiveFunctionValue(
      String primitiveName, FunctionTypeValue type) { 
    super(type, new ArrayList<Expression>(0));
    this.primitiveName = primitiveName;
  }
  
  @Override
  public boolean isRuntimeKnowable() {
    return true;
  }
  
  @Override
  public boolean isElaborationtimeKnowable() {
    return false;
  }
  
  @Override
  public void accept(ValueVisitor visitor) {
    visitor.visit(this);
  }
  
}
