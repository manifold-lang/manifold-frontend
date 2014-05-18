package org.whdl.intermediate;

import java.util.ArrayList;
import java.util.List;

public class ArrayValue extends Value {

  private Type elementType;
  private List<Value> contents = null;
  
  public ArrayValue(Type elementType){
    super(new ArrayType(elementType));
    this.elementType = elementType;
  }
  
  public void setContents(List<Value> newContents) throws TypeException{
	  // type-check contents -- every Value must have type 'elementType'
	  for(Value v : newContents){
		  Type t = v.getType();
		  if(!t.equals(elementType)){
			  throw new TypeException(elementType.toString(), t.toString());
		  }
	  }
	  // now we can copy the new list into our object
	  contents = new ArrayList<Value>(newContents);
  }
  
  public Value get(Integer i){
	  return contents.get(i);
  }
  
  public Integer length(){
	  return contents.size();
  }
  
}
