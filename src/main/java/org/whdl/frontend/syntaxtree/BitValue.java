package org.whdl.frontend.syntaxtree;

public class BitValue extends Value {

  private static final BitValue highInstance = new BitValue(true);
  private static final BitValue lowInstance = new BitValue(false);

  public static BitValue getInstance(boolean value) {
    if (value) {
      return highInstance;
    } else {
      return lowInstance;
    }
  }

  private boolean value;

  private BitValue(boolean value) {
    this.value = value;
  }

  public TypeValue getType() {
    return BitTypeValue.getInstance();
  }

  public boolean toBoolean() {
    return value;
  }
  
  public void verify() {}
  
  public boolean isCompiletimeEvaluable() {
    return true;
  }
  
  public boolean isSynthesizable() {
    return true;
  }
}
