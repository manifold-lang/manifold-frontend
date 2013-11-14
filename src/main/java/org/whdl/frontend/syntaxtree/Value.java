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
   * Either this or isRuntimeEvaluable or both must return true.
   */
  public abstract boolean isCompiletimeEvaluable();
  
  /*
   * Returns true if this value is synthesizable.
   */
  public abstract boolean isRuntimeEvaluable();
}
