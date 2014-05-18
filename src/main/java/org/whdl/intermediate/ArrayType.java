package org.whdl.intermediate;


public class ArrayType extends Type {
  private Type elementType;
  public Type getElementType(){
    return this.elementType;
  }
  
  public ArrayType(String typename, Type elementType){
    super(typename);
    this.elementType = elementType;
  }

  @Override
  public Value instantiate() {
    return new ArrayValue(this);
  }
}
