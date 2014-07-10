package org.manifold.compiler.front;

import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

public abstract class Expression {
  public abstract TypeValue getType();
  public abstract Value evaluate();
  public abstract void verify() throws Exception;
  public abstract boolean isAssignable();
  public abstract boolean isCompiletimeEvaluable();
  public abstract boolean isSynthesizable();
}
