package org.whdl.frontend;

public class TypeTypeValue extends TypeValue {

  public final static TypeTypeValue instance = new TypeTypeValue();

  private TypeTypeValue() {}

  public TypeTypeValue getTypeValue() {
    return instance;
  }

}