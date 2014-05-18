package org.whdl.intermediate;

public class ArrayValue extends Value {

  private Type elementType;
  
  public ArrayValue(Type elementType){
    super(new ArrayType(elementType));
    this.elementType = elementType;
  }
  
  // FIXME functions and internals on arrays: getting and setting their contents, getting length, etc.
  
}
