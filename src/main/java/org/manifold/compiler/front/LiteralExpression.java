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
  public Value getValue(Scope scope) {
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
  public boolean isElaborationtimeKnowable(Scope scope) {
    return value.isElaborationtimeKnowable();
  }

  @Override
  public boolean isRuntimeKnowable(Scope scope) {
    return value.isRuntimeKnowable();
  }

}
