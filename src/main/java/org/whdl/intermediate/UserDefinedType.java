package org.whdl.intermediate;


public class UserDefinedType extends Type {
  private Type type;
  
  public UserDefinedType(Type type){
    this.type = type;
  }

  @Override
  public Value instantiate() {
    return type.instantiate();
  }
}
