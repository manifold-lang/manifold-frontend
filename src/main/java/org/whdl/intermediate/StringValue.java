package org.whdl.intermediate;


public class StringValue extends Value {

  private String val;
  public StringValue(String val){
    this.val = val;
  }
  
  @Override
  public Type acceptVisitor(ValueTypeVisitor v) {
    return v.visit(this);
  }
  
}
