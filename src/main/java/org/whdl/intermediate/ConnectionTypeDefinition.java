package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;

public class ConnectionTypeDefinition extends TypeDefinition {
  private Map<String, TypeTypeDefinition> attributes;
  
  public ConnectionTypeDefinition(String typename){
    super(typename);    
    this.attributes = new HashMap<String, TypeTypeDefinition>();
  }
}
