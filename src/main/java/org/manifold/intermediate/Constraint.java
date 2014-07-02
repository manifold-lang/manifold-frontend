package org.manifold.intermediate;


public class Constraint extends Value {

  private final Attributes attributes;

  public Value getAttribute(String attrName) throws
      UndeclaredAttributeException {
    return attributes.get(attrName);
  }

  public void setAttribute(String attrName, Value attrValue) {
    attributes.put(attrName, attrValue);
  }

  public Constraint(ConstraintType type){
    super(type);
    this.attributes = new Attributes();
  }

}
