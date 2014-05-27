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
 
}
