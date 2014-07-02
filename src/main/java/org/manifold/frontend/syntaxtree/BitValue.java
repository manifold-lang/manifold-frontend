package org.manifold.frontend.syntaxtree;

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

  private final boolean value;

  private BitValue(boolean value) {
    this.value = value;
  }

  @Override
  public TypeValue getType() {
    return BitTypeValue.getInstance();
  }

  public boolean toBoolean() {
    return value;
  }

  @Override
  public void verify() { }

  @Override
  public boolean isCompiletimeEvaluable() {
    return true;
  }

  @Override
  public boolean isSynthesizable() {
    return true;
  }
}
