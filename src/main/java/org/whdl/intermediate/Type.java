package org.whdl.intermediate;

public abstract class Type {

  private String typename;
  public String getTypename() {
    return typename;
  }
  
  public Type(String typename){
    this.typename = typename;
  }
	
  public abstract Value instantiate();
  
  @Override
  public boolean equals(Object other){
    // Every value of the same type references the same type object;
    // therefore the following comparison is exactly what we need.
    return (this == other);
  }
  
}
