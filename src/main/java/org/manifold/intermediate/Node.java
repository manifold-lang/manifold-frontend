package org.manifold.intermediate;

import java.util.HashMap;
import java.util.Map;

public class Node extends Value{
  private Attributes attributes;

  public Value getAttribute(String attrName) throws UndeclaredAttributeException {
    return attributes.get(attrName);
  }

  public void setAttribute(String attrName, Value attrValue) {
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
    this.attributes = new Attributes();
    this.ports = new HashMap<String, Port>();
  }

}
