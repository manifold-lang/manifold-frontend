package org.manifold.intermediate;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class Value {
  private Type type = null;
  public Type getType(){
    return type;
  }
  
  public Value(Type type){
    this.type = checkNotNull(type);
  }
}
