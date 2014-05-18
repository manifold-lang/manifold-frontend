package org.whdl.intermediate;


public class TypeTypeDefinition extends TypeDefinition {
  private Type type;
  
  public TypeTypeDefinition(String typename, Type type){
    super(typename);
    this.type = type;
  }

  @Override
  public Value instantiate() {
    return type.instantiate();
  }
}
