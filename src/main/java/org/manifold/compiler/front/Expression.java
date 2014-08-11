package org.manifold.compiler.front;

import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

public abstract class Expression {
  public abstract TypeValue getType(Scope scope);
  public abstract Value getValue(Scope scope);
  public abstract void verify(Scope scope) throws Exception;
  public abstract boolean isAssignable();
  public abstract boolean isElaborationtimeKnowable(Scope scope);
  public abstract boolean isRuntimeKnowable(Scope scope);
  public abstract void accept(ExpressionVisitor visitor);
  
  public <T extends Value> T evaluate(Scope scope, Class<T> type) {
    return (T) getValue(scope);
  }
}
