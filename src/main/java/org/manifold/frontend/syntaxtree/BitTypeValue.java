package org.manifold.frontend.syntaxtree;

public class BitTypeValue extends TypeValue {

  private final static BitTypeValue instance = new BitTypeValue();

  public static BitTypeValue getInstance() {
    return instance;
  }

  private BitTypeValue() {}
 
  public void verify() {}
}