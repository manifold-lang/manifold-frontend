package org.whdl.intermediate.types;

import org.whdl.intermediate.Type;

public class ArrayType extends Type {
  private Type elementType;
  public Type getElementType(){
    return this.elementType;
  }
  
  public ArrayType(Type elementType){
    this.elementType = elementType;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((elementType == null) ? 0 : elementType.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    ArrayType other = (ArrayType) obj;
    if (elementType == null) {
      if (other.elementType != null) {
        return false;
      }
    } else if (!elementType.equals(other.elementType)) {
      return false;
    }
    return true;
  }
}
