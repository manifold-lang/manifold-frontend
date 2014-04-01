package org.whdl.intermediate.definitions;

import java.util.HashMap;
import java.util.Map;

import org.whdl.intermediate.Definition;

public class NodeDefinition extends Definition {
  private Map<String, TypeDefinition> attributes;
  private Map<String, EndpointDefinition> endpoints;
  
  public NodeDefinition(String typename){
    super(typename);
    this.attributes = new HashMap<String, TypeDefinition>();
    this.endpoints = new HashMap<String, EndpointDefinition>();
  }
}
