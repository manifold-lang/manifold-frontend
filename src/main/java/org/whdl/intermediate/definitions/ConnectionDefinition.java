package org.whdl.intermediate.definitions;

import java.util.HashMap;
import java.util.Map;

import org.whdl.intermediate.Definition;

public class ConnectionDefinition extends Definition {
  private Map<String, TypeDefinition> attributes;
  
  public ConnectionDefinition(String typename){
    super(typename);    
    this.attributes = new HashMap<String, TypeDefinition>();
  }
}
