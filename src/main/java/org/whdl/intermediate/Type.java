package org.whdl.intermediate;

public abstract class Type {
	
  public abstract Value instantiate();
  
  @Override
  public boolean equals(Object other){
    // Every value of the same type references the same type object;
    // therefore the following comparison is exactly what we need.
    return (this == other);
  }
  
}
