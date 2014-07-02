package org.manifold.intermediate;

public class StringType extends Type {

  private static final StringType instance = new StringType();

  private StringType() { }

  public static StringType getInstance() {
    return instance;
  }
}
