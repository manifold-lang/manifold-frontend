package org.whdl.intermediate;


public class IntegerValue extends Value {

  private Integer val;
  public IntegerValue(String instanceName, Integer val){
    super(instanceName);
    this.val = val;
  }
  
  @Override
  public Type acceptVisitor(ValueTypeVisitor v){
    return v.visit(this);
  }
  
}
