package org.manifold.intermediate;


public class ArrayType extends Type {
  private Type elementType;
  public Type getElementType(){
    return this.elementType;
  }
  
  public ArrayType(Type elementType){
    this.elementType = elementType;
  }
 
}
