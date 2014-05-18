package org.whdl.intermediate;

import org.whdl.intermediate.PrimitiveType.PrimitiveKind;


public class StringValue extends Value {

  private String val;
  public StringValue(String val){
    super(new PrimitiveType(PrimitiveKind.STRING));
    this.val = val;
  }

}
