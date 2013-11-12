package org.whdl.frontend;

public class BooleanTypeValue extends TypeValue {

  private final static BooleanTypeValue instance = new BooleanTypeValue();

  public static BooleanTypeValue getInstance() {
    return instance;
  }

  private BooleanTypeValue() {}

}