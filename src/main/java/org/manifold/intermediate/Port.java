package org.manifold.intermediate;

import static com.google.common.base.Preconditions.checkNotNull;

public class Port extends Value {

  private Attributes attributes;
  private Node parent;

  public Value getAttribute(String attrName) throws UndeclaredAttributeException {
    return attributes.get(attrName);
  }
  public void setAttribute(String attrName, Value attrValue){
    attributes.put(attrName, attrValue);
  }
  
  public Node getParent() {
    return parent;
  }

  public Port(PortType type, Node parent){
    super(type);
    this.attributes = new Attributes();
    this.parent = checkNotNull(parent);
  }
}
