package org.manifold.intermediate;


public class StringValue extends Value {

  private String val;
  public StringValue(Type t, String val){
    super(t);
    this.val = val;
  }

}
