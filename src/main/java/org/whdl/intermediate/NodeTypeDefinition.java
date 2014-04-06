package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;

public class NodeTypeDefinition extends TypeDefinition {
  private Map<String, TypeTypeDefinition> attributes;
  private Map<String, EndpointTypeDefinition> endpoints;
  
  public NodeTypeDefinition(String typename){
    super(typename);
    this.attributes = new HashMap<String, TypeTypeDefinition>();
    this.endpoints = new HashMap<String, EndpointTypeDefinition>();
  }
}
