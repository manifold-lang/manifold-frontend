package org.manifold.intermediate;


public class Connection extends Value {

  private Attributes attributes;

  public Value getAttribute(String attrName) throws UndeclaredAttributeException {
    return attributes.get(attrName);
  }
  public void setAttribute(String attrName, Value attrValue){
    attributes.put(attrName, attrValue);
  }
  private Port portFrom = null, portTo = null;

  public Connection(ConnectionType type){
    super(type);
    this.attributes = new Attributes();
  }

}
