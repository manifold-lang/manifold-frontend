package org.whdl.intermediate.definitions;

import java.util.HashMap;
import java.util.Map;

public class NodeDefinition {
  private String typename;
  public String getTypename(){
    return typename;
  }
  private Map<String, TypeDefinition> attributes;
  private Map<String, EndpointDefinition> endpoints;
  
  public NodeDefinition(String typename){
    this.typename = typename;
    this.attributes = new HashMap<String, TypeDefinition>();
    this.endpoints = new HashMap<String, EndpointDefinition>();
  }
}
