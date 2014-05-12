package org.whdl.intermediate;


public class TypeTypeDefinition extends TypeDefinition {
  private Type type;
  
  public TypeTypeDefinition(String typename, Type type){
    super(typename);
    this.type = type;
  }

  @Override
  public Value instantiate(String instanceName) {
    return type.instantiate(instanceName);
  }
}
