package org.whdl.intermediate;

import java.util.ArrayList;
import java.util.List;


public class ArrayType extends Type {
  private Type elementType;
  public Type getElementType(){
    return this.elementType;
  }
  
  public ArrayType(Type elementType){
    this.elementType = elementType;
  }

  @Override
  public Value instantiate() throws TypeMismatchException{
    // FIXME is it correct to assume the array is empty if we have no contents? or should we throw an exception?
    return new ArrayValue(this, new ArrayList<Value>());
  }
  
  public Value instantiate(List<Value> contents) throws TypeMismatchException {
    return new ArrayValue(this, contents);
  }
}
