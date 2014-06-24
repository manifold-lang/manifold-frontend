package org.manifold.intermediate;

public class Port extends Value {

  private Attributes attributes;
  private Node parent = null;

  public Value getAttribute(String attrName) throws UndeclaredAttributeException {
    return attributes.get(attrName);
  }
  public void setAttribute(String attrName, Value attrValue){
    attributes.put(attrName, attrValue);
  }

  public Port(PortType type){
    super(type);
    this.attributes = new Attributes();
  }

}
