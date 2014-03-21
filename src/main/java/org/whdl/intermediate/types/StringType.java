package org.whdl.intermediate.types;

import org.whdl.intermediate.Type;


public class StringType implements Type {
  public boolean equals(Object that){
    if(that == null) return false;
    if(this == that) return true;
    return this.getClass() == that.getClass();
  }

}
