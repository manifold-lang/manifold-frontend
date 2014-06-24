package org.manifold.intermediate;

public class BooleanType extends Type {
  private static final BooleanType instance = new BooleanType();

  private BooleanType() {

  }

  public static BooleanType getInstance() {
    return instance;
  }
}
