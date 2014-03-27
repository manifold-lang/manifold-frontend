package org.whdl.intermediate.definitions;

import org.whdl.intermediate.Type;

public class TypeDefinition {
  private String typename;
  public String getTypename(){
    return typename;
  }
  private Type type;
  
  public TypeDefinition(String typename, Type type){
    this.typename = typename;
    this.type = type;
  }
}
