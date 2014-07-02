package org.manifold.intermediate;

import java.util.HashMap;
import java.util.Map;

public class Node extends Value {

  private final Attributes attributes;
  private final Map<String, Port> ports;

  public Value getAttribute(String attrName)
      throws UndeclaredAttributeException {
    return attributes.get(attrName);
  }

  public void setAttribute(String attrName, Value attrValue) {
    attributes.put(attrName, attrValue);
  }


  public Port getPort(String portName) throws UndeclaredIdentifierException{
    if (ports.containsKey(portName)){
      return ports.get(portName);
    } else {
      throw new UndeclaredIdentifierException(
        "no port named '" + portName + "'"
      );
    }
  }
  public void setPortAttributes(
      String portName,
      String attrName,
      Value attrValue
  ) throws UndeclaredIdentifierException {

    if (ports.containsKey(portName)) {
      ports.get(portName).setAttribute(attrName, attrValue);
    } else {
      throw new UndeclaredIdentifierException(
        "no port named '" + portName + "'"
      );
    }
  }

  public Node(NodeType type){
    super(type);
    this.attributes = new Attributes();
    this.ports = new HashMap<>();

    if (type.getPorts() != null) {
      for (Map.Entry<String, PortType> portEntry : type.getPorts().entrySet()) {
        this.ports.put(
          portEntry.getKey(),
          new Port(portEntry.getValue(), this)
        );
      }
    }
  }

}
