package org.whdl.intermediate;

public class IntegerType extends Type {

  public Value instantiate(){
    return new IntegerValue(this, 0);
  }
  
}
