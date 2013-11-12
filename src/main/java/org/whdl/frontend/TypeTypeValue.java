package org.whdl.frontend;

public class TypeTypeValue extends TypeValue {

  private final static TypeTypeValue instance = new TypeTypeValue();

  public static TypeTypeValue getInstance() {
    return instance;
  }

  private TypeTypeValue() {}

  public TypeTypeValue getTypeValue() {
    return instance;
  }

}