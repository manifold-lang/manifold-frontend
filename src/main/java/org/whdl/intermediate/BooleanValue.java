package org.whdl.intermediate;


public class BooleanValue extends Value {

  private Boolean val;
  public BooleanValue(String instanceName, Boolean val){
    super(instanceName);
    this.val = val;
  }
  
  @Override
  public Type acceptVisitor(ValueTypeVisitor v) {
    return v.visit(this);
  }

}
