package org.whdl.intermediate;

import org.whdl.intermediate.types.PrimitiveType;

public class BooleanValue extends Value {

  private Boolean val;
  public BooleanValue(Boolean val){
    this.val = val;
  }
  
  @Override
  public Type acceptVisitor(ValueTypeVisitor v) {
    return v.visit(this);
  }

}
