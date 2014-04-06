package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;

public class EndpointTypeDefinition extends TypeDefinition {
  private Map<String, TypeTypeDefinition> attributes;
  
  public EndpointTypeDefinition(String typename){
    super(typename);
    this.attributes = new HashMap<String, TypeTypeDefinition>();
  }
}
