package org.whdl.intermediate;


public class Constraint extends Value {

  private Attributes attributes;

  public Value getAttribute(String attrName) throws UndeclaredAttributeException {
    return attributes.get(attrName);
  }

  public void setAttribute(String AttrName, Value AttrValue) {
    attributes.put(AttrName, AttrValue);
  }

  public Constraint(ConstraintType type){
    super(type);
    this.attributes = new Attributes();
  }

}
