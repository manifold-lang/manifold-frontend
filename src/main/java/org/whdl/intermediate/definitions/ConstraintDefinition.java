package org.whdl.intermediate.definitions;

import java.util.HashMap;
import java.util.Map;

import org.whdl.intermediate.Definition;
import org.whdl.intermediate.DomainObject;

public class ConstraintDefinition extends Definition {
  private Map<String, TypeDefinition> arguments;
  
  public ConstraintDefinition(String typename){
    super(typename);
    this.arguments = new HashMap<String, TypeDefinition>();
  }
}
