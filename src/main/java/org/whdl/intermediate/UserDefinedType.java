package org.whdl.intermediate;


public class UserDefinedType extends Type {
  private Type type;
  
  public UserDefinedType(String typename, Type type){
    super(typename);
    this.type = type;
  }

  @Override
  public Value instantiate() {
    return type.instantiate();
  }
}
