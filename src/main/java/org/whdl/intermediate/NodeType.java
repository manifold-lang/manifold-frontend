package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class NodeType extends Type {
 
  private Map<String, Type> attributes;
  private Map<String, PortType> endpoints;
  
  public NodeType(Map<String, Type> attributes, Map<String, PortType> endpoints){
    this.attributes = new HashMap<String, Type>(attributes);
    this.endpoints = new HashMap<String, PortType>(endpoints);
  }
  
}
