package org.whdl.frontend.syntaxtree;

public abstract class Value {

  public abstract TypeValue getTypeValue();
  
  /*
   * Executed during formal verification pass. Any errors should result in an
   * exception.
   */
  public void verify() {}

}
