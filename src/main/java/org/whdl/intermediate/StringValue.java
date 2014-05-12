package org.whdl.intermediate;

import org.whdl.intermediate.PrimitiveType.PrimitiveKind;


public class StringValue extends Value {

  private String val;
  public StringValue(String instanceName, String val){
    super(new PrimitiveType(PrimitiveKind.STRING), instanceName);
    this.val = val;
  }

}
