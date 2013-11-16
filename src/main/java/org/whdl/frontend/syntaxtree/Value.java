package org.whdl.frontend.syntaxtree;

public abstract class Value {

  public abstract TypeValue getType();
  
  /*
   * Executed during formal verification pass. Any errors should result in an
   * exception.
   */
  public abstract void verify();

}
