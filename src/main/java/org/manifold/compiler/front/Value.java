package org.manifold.compiler.front;

public abstract class Value {

  public abstract TypeValue getType();
  
  /*
   * Executed during formal verification pass. Any errors should result in an
   * exception.
   */
  public abstract void verify() throws Exception;
  
  /*
   * Returns true if this value can be evaulated at compiletime.
   * Either this or isSynthesizable or both must return true.
   */
  public abstract boolean isCompiletimeEvaluable();
  
  /*
   * Returns true if this value is synthesizable, able to be represented in
   * hardware.
   */
  public abstract boolean isSynthesizable();
}
