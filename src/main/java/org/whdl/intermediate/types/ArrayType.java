package org.whdl.intermediate.types;

import org.whdl.intermediate.Type;

public class ArrayType implements Type {
  private Type elementType;
  public Type getElementType(){
    return this.elementType;
  }
  
  public ArrayType(Type elementType){
    this.elementType = elementType;
  }
}
