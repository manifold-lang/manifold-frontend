package org.whdl.intermediate.definitions;

import org.whdl.intermediate.Definition;
import org.whdl.intermediate.Type;

public class TypeDefinition extends Definition {
  private Type type;
  
  public TypeDefinition(String typename, Type type){
    super(typename);
    this.type = type;
  }
}
