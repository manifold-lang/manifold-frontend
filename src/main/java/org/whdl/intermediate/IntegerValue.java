package org.whdl.intermediate;

import org.whdl.intermediate.PrimitiveType.PrimitiveKind;


public class IntegerValue extends Value {

  private Integer val;
  public IntegerValue(Integer val){
    super(new PrimitiveType(PrimitiveKind.INTEGER));
    this.val = val;
  }
  
}
