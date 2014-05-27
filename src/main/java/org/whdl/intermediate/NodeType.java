package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class NodeType extends Type {
 
  private Map<String, Type> attributes;
  private Map<String, EndpointType> endpoints;
  
  public NodeType(Map<String, Type> attributes, Map<String, EndpointType> endpoints){
    this.attributes = new HashMap<String, Type>(attributes);
    this.endpoints = new HashMap<String, EndpointType>(endpoints);
  }
  
}
