package org.whdl.frontend.syntaxtree;

public class TypeTypeValue extends TypeValue {

  private final static TypeTypeValue instance = new TypeTypeValue();

  public static TypeTypeValue getInstance() {
    return instance;
  }

  private TypeTypeValue() {}

  public TypeTypeValue getType() {
    return instance;
  }
  
  public void verify() {}
}