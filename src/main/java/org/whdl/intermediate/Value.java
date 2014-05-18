package org.whdl.intermediate;


public abstract class Value {
  private Type type = null;
  public Type getType(){
    return type;
  }
  
  public Value(Type type){
    this.type = type;
  }
}
