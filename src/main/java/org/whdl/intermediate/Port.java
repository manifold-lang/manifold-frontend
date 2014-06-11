package org.whdl.intermediate;

public class Port extends Value {

  private AttributeSet attributes;
  private Node parent = null;

  public Value getAttribute(String attrName) throws UndeclaredIdentifierException{
    return attributes.get(attrName);
  }
  public void setAttribute(String attrName, Value attrValue){
    attributes.put(attrName, attrValue);
  }

  public Port(PortType type){
    super(type);
    this.attributes = new AttributeSet();
  }

}
