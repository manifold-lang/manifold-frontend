package org.manifold.intermediate;

public class IntegerType extends Type {
  private static final IntegerType instance = new IntegerType();

  private IntegerType() {

  }

  public static IntegerType getInstance() {
    return instance;
  }
}
