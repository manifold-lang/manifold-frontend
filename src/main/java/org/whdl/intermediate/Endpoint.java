package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;

public class Endpoint extends Value {
  private EndpointTypeDefinition definition;
  public EndpointTypeDefinition getDefinition(){
    return this.definition;
  }
  
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
  private Node parent = null;
  
  public Endpoint(String instanceName, EndpointTypeDefinition definition){
    super(new EndpointType(definition), instanceName);
    this.definition = definition;
    this.attributes = new HashMap<String, Value>();
  }

}
