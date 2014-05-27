package org.whdl.intermediate;

import java.util.ArrayList;
import java.util.List;

public class ArrayValue extends Value {

  private Type elementType;
  private List<Value> contents = null;
  
  public ArrayValue(ArrayType t, List<Value> contents) throws TypeMismatchException{
    super(t);
    this.elementType = t.getElementType();
    // type-check contents -- every Value must have type 'elementType'
    for(Value v : contents){
      Type vt = v.getType();
      if(!vt.equals(elementType)){
        throw new TypeMismatchException(elementType, vt);
      }
    }
    // now we can copy the new list into our object
    this.contents = new ArrayList<Value>(contents);
  }

  public Type getElementType(){
    return this.elementType;
  }
  
  public Value get(Integer i){
	  return contents.get(i);
  }
  
  public Integer length(){
	  return contents.size();
  }
  
}
