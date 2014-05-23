package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class NodeType extends Type {
 
  private Map<String, UserDefinedType> attributes;
  private Map<String, EndpointType> endpoints;
  
  public NodeType(){
    this.attributes = new HashMap<String, UserDefinedType>();
    this.endpoints = new HashMap<String, EndpointType>();
  }
  
  @Override
  public Value instantiate() {
    Node node = new Node(this);
    // elaborate default attributes
    for(Entry<String, UserDefinedType> attr : attributes.entrySet()){
      String attrName = attr.getKey();
      UserDefinedType attrTypeValue = attr.getValue();
      Value attrValue = attrTypeValue.instantiate();
      node.setAttribute(attrName, attrValue);
    }
    return node;
  }
  
  public void addAttribute(String attrName, UserDefinedType attrType) {
    // FIXME multiple additions of the same attribute?
    attributes.put(attrName, attrType);
  }
  
}
