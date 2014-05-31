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
  
  private Map<String, Port> ports;
  public Port getPort(String portName) throws UndeclaredIdentifierException{
    if(ports.containsKey(portName)){
      return ports.get(portName);
    }else{
      throw new UndeclaredIdentifierException("no port named '" + portName + "'");
    }
  }
  public void setPort(String portName, Port portValue){
    ports.put(portName, portValue);
  }
  
  public Node(Type type){
    super(type);
    this.attributes = new HashMap<String, Value>();
    this.ports = new HashMap<String, Port>();
  }

}
