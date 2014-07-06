package org.manifold.compiler;

public class StringType extends Type {

  private static final StringType instance = new StringType();

  private StringType() { }

  public static StringType getInstance() {
    return instance;
  }
}
