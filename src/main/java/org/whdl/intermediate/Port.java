package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;

public class Port extends Value {
 
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
  
  public Port(PortType type){
    super(type);
    this.attributes = new HashMap<String, Value>();
  }

}
