package org.whdl.intermediate;

import org.whdl.intermediate.types.PrimitiveType;

public class IntegerValue extends Value {

  private Integer val;
  public IntegerValue(Integer val){
    this.val = val;
  }
  
  @Override
  public Type acceptVisitor(ValueTypeVisitor v){
    return v.visit(this);
  }
  
}
