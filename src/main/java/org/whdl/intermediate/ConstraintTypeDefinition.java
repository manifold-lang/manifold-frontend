package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;

public class ConstraintTypeDefinition extends TypeDefinition {
  private Map<String, TypeTypeDefinition> arguments;
  
  public ConstraintTypeDefinition(String typename){
    super(typename);
    this.arguments = new HashMap<String, TypeTypeDefinition>();
  }
}
