package org.whdl.intermediate;

public class StringType extends Type {
  
  public Value instantiate(){
    return new StringValue(this, "");
  }

}
