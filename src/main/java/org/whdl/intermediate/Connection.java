package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;

public class Connection extends Value {
  private ConnectionTypeDefinition definition;
  public ConnectionTypeDefinition getDefinition(){
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
  private Endpoint endpointFrom = null, endpointTo = null;
  
  public Connection(String instanceName, ConnectionTypeDefinition definition){
    super(new ConnectionType(definition), instanceName);
    this.definition = definition;
    this.attributes = new HashMap<String, Value>();
  }

}
