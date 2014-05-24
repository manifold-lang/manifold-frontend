package org.whdl.intermediate;

public class BooleanType extends Type {

  public Value instantiate(){
    return new BooleanValue(this, false);
  }

}
