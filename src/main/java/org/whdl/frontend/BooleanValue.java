package org.whdl.frontend;

public class BooleanValue extends Value {

  private static final BooleanValue trueInstance = new BooleanValue(true);
  private static final BooleanValue falseInstance = new BooleanValue(false);

  private boolean value;

  private BooleanValue(boolean value) {
    this.value = value;
  }

  public TypeValue getTypeValue() {
    return BooleanTypeValue.instance;
  }

  public boolean toBoolean() {
    return value;
  }

}
