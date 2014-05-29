package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;

public class Node extends Value{
  private Map<String, Value> attributes;
  public Value getAttribute(String attrName) throws UndeclaredIdentifierException{
    if(attributes.containsKey(attrName)){
      return attributes.get(attrName);
    }else{
      throw new UndeclaredIdentifierException("no attribute named '" + attrName + "'");
    }
  }
  public void setAttribute(String attrName, Value attrValue){
    attributes.put(attrName, attrValue);
  }
  
  private Map<String, Port> endpoints;
  public Port getEndpoint(String eptName) throws UndeclaredIdentifierException{
    if(endpoints.containsKey(eptName)){
      return endpoints.get(eptName);
    }else{
      throw new UndeclaredIdentifierException("no endpoint named '" + eptName + "'");
    }
  }
  public void setEndpoint(String eptName, Port eptValue){
    endpoints.put(eptName, eptValue);
  }
  
  public Node(Type type){
    super(type);
    this.attributes = new HashMap<String, Value>();
    this.endpoints = new HashMap<String, Port>();
  }

}
