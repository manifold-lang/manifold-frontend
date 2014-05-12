package org.whdl.intermediate;

import org.whdl.intermediate.PrimitiveType.PrimitiveKind;


public class IntegerValue extends Value {

  private Integer val;
  public IntegerValue(String instanceName, Integer val){
    super(new PrimitiveType(PrimitiveKind.INTEGER), instanceName);
    this.val = val;
  }
  
}
