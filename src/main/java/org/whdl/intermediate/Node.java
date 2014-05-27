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
  
  private Map<String, Endpoint> endpoints;
  public Endpoint getEndpoint(String eptName) throws UndeclaredIdentifierException{
    if(endpoints.containsKey(eptName)){
      return endpoints.get(eptName);
    }else{
      throw new UndeclaredIdentifierException("no endpoint named '" + eptName + "'");
    }
  }
  public void setEndpoint(String eptName, Endpoint eptValue){
    endpoints.put(eptName, eptValue);
  }
  
  public Node(Type type){
    super(type);
    this.attributes = new HashMap<String, Value>();
    this.endpoints = new HashMap<String, Endpoint>();
  }

}
