package org.whdl.intermediate;

public abstract class Definition {
  private String typename;
  public String getTypename() {
    return typename;
  }
  
  public Definition(String typename){
    this.typename = typename;
  }
}
