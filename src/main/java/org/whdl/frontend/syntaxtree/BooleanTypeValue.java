package org.whdl.frontend.syntaxtree;

public class BooleanTypeValue extends TypeValue {

  private final static BooleanTypeValue instance = new BooleanTypeValue();

  public static BooleanTypeValue getInstance() {
    return instance;
  }

  private BooleanTypeValue() {}

}