package org.whdl.intermediate;

import org.whdl.intermediate.PrimitiveType.PrimitiveKind;


public class BooleanValue extends Value {

  private Boolean val;
  public BooleanValue(String instanceName, Boolean val){
    super(new PrimitiveType(PrimitiveKind.BOOLEAN), instanceName);
    this.val = val;
  }

}
