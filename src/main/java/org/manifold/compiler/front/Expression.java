package org.manifold.compiler.front;

import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

public abstract class Expression {
  public abstract TypeValue getType(Scope scope);
  public abstract Value evaluate(Scope scope);
  public abstract void verify(Scope scope) throws Exception;
  public abstract boolean isAssignable();
  public abstract boolean isCompiletimeEvaluable(Scope scope);
  public abstract boolean isSynthesizable(Scope scope);
  
  public <T extends Value> T evaluate(Scope scope, Class<T> type) {
    return (T) evaluate(scope);
  }
}
