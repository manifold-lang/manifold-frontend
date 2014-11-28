package org.manifold.compiler.front;

import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

public abstract class Expression {
  // TODO most everything in here should take a namespace map instead of
  // a scope, and additionally some kind of "default namespace"
  // so we know where this expression was defined
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
