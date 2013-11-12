package org.whdl.frontend;

public class BooleanValue extends Value {

  private final BooleanValue trueInstance = new BooleanValue(true);
  private final BooleanValue falseInstance = new BooleanValue(false);

  private Boolean value;

  private BooleanValue(Boolean value) {
    this.value = value;
  }

  public TypeValue getTypeValue() {
    return BooleanTypeValue.instance;
  }

  public Boolean toBoolean() {
    return value;
  }

}
