package org.whdl.intermediate;


public abstract class Value {
  private Type type = null;
  public Type getType(){
    if(type == null){
      throw new UndefinedBehaviourError("attempted to get the type of an Expression whose type had not yet been assigned");
    }else{
      return type;
    }
  }
  public void setType(Type t){
    if(t == null){
      throw new UndefinedBehaviourError("attempted to set the type of an Expression to null");
    }
    if(this.type != null){
      throw new UndefinedBehaviourError("attempted to change the type of an Expression whose type was already assigned");
    }
    this.type = t;
  }
  
  public abstract Type acceptVisitor(ValueTypeVisitor v);
  
  private String instanceName;
  public String getInstanceName(){
    return instanceName;
  }
  public Value(String instanceName){
    this.instanceName = instanceName;
  }
}
