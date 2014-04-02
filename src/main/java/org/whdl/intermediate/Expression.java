package org.whdl.intermediate;

import org.whdl.intermediate.exceptions.UndefinedBehaviourError;

public abstract class Expression {
  private Type type = null;
  public Type getType(){
    if(type == null){
      throw new UndefinedBehaviourError("attempted to get the type of an Expression whose type had not yet been assigned");
    }else{
      return type;
    }
  }
  public void setType(Type t){
    this.type = t;
  }
  
  public abstract Type accept(ExprTypeVisitor v);
  public abstract Object accept(ExprEvalVisitor v);
}
