package org.whdl.intermediate;


public abstract class Value {
  private Type type = null;
  public Type getType(){
    return type;
  }
  
  private String instanceName;
  public String getInstanceName(){
    return instanceName;
  }
  public Value(Type type, String instanceName){
    this.type = type;
    this.instanceName = instanceName;
  }
}
