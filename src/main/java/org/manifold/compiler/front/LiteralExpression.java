package org.manifold.compiler.front;

import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

public class LiteralExpression extends Expression {
  private Value value;

  public LiteralExpression(Value value) {
    this.value = value;
  }

  @Override
  public TypeValue getType(Scope scope) {
    return value.getType();
  }

  @Override
  public Value evaluate(Scope scope) {
    return value;
  }

  @Override
  public void verify(Scope scope) throws Exception{
    value.verify();
  }

  @Override
  public boolean isAssignable() {
    return false;
  }

  @Override
  public boolean isCompiletimeEvaluable(Scope scope) {
    return value.isElaborationtimeKnowable();
  }

  @Override
  public boolean isSynthesizable(Scope scope) {
    return value.isRuntimeKnowable();
  }

}
