package org.whdl.intermediate;

import org.whdl.intermediate.PrimitiveType.PrimitiveKind;


public class BooleanValue extends Value {

  private Boolean val;
  public BooleanValue(Boolean val){
    super(new PrimitiveType(PrimitiveKind.BOOLEAN));
    this.val = val;
  }

}
