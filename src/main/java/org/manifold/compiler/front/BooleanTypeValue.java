package org.manifold.compiler.front;

public class BooleanTypeValue extends TypeValue {

  private static final BooleanTypeValue instance = new BooleanTypeValue();

  public static BooleanTypeValue getInstance() {
    return instance;
  }

  private BooleanTypeValue() { }

  public void verify() { }
}
