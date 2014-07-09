package org.manifold.compiler.front;

import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

public class LiteralExpression extends Expression {
  private Value value;

  public LiteralExpression(Value value) {
    this.value = value;
  }

  @Override
  public TypeValue getType() {
    return value.getType();
  }

  @Override
  public Value evaluate() {
    return value;
  }

  @Override
  public void verify() throws Exception{
    value.verify();
  }

  @Override
  public boolean isAssignable() {
    return false;
  }

  @Override
  public boolean isCompiletimeEvaluable() {
    return value.isCompiletimeEvaluable();
  }

  @Override
  public boolean isSynthesizable() {
    return value.isSynthesizable();
  }

}
