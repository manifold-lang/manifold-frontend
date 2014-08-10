package org.manifold.compiler;

public class UnitTypeValue extends TypeValue {
  private static final UnitTypeValue instance = new UnitTypeValue();

  public static UnitTypeValue getInstance() {
    return instance;
  }

  private UnitTypeValue() { }
}
