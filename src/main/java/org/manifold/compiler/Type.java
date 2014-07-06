package org.manifold.compiler;

public abstract class Type {
  
  @Override
  public boolean equals(Object other){
    // Every value of the same type references the same type object;
    // therefore the following comparison is exactly what we need.
    return (this == other);
  }
  
}
