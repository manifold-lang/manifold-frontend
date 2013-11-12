package org.whdl.frontend;

public class BooleanValue extends Value {

  private static final BooleanValue trueInstance = new BooleanValue(true);
  private static final BooleanValue falseInstance = new BooleanValue(false);

  public static BooleanValue getInstance(boolean value) {
    if (value) {
      return trueInstance;
    } else {
      return falseInstance;
    }
  }

  private boolean value;

  private BooleanValue(boolean value) {
    this.value = value;
  }

  public TypeValue getTypeValue() {
    return BooleanTypeValue.getInstance();
  }

  public boolean toBoolean() {
    return value;
  }

}
