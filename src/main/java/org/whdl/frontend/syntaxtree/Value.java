package org.whdl.frontend.syntaxtree;

public abstract class Value {

  public abstract TypeValue getType();
  
  /*
   * Executed during formal verification pass. Any errors should result in an
   * exception.
   */
  public abstract void verify();
  
  /*
   * Returns true if this value can be evaulated at compiletime.
   */
  public abstract boolean isCompiletimeEvaluable();
  
  /*
   * Returns true if this value is syntesizable.
   */
  public abstract boolean isRuntimeEvaluable();
}
