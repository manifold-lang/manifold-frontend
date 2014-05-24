package org.whdl.intermediate;


public class IntegerValue extends Value {

  private Integer val;
  public IntegerValue(Type t, Integer val){
    super(t);
    this.val = val;
  }
  
}
