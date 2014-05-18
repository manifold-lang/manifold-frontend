package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class NodeTypeDefinition extends TypeDefinition {
  private Map<String, TypeTypeDefinition> attributes;
  private Map<String, EndpointTypeDefinition> endpoints;
  
  public NodeTypeDefinition(String typename){
    super(typename);
    this.attributes = new HashMap<String, TypeTypeDefinition>();
    this.endpoints = new HashMap<String, EndpointTypeDefinition>();
  }
  
  @Override
  public Value instantiate() {
    Node node = new Node(this);
    // elaborate default attributes
    for(Entry<String, TypeTypeDefinition> attr : attributes.entrySet()){
      String attrName = attr.getKey();
      TypeTypeDefinition attrTypeValue = attr.getValue();
      Value attrValue = attrTypeValue.instantiate();
      node.setAttribute(attrName, attrValue);
    }
    return node;
  }
  
  public void addAttribute(String attrName, TypeTypeDefinition attrType) {
    // FIXME multiple additions of the same attribute?
    attributes.put(attrName, attrType);
  }
  
}
