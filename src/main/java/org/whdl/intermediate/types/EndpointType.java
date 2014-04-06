package org.whdl.intermediate.types;

import org.whdl.intermediate.Type;

public class EndpointType extends Type {
  private String definitionName;
  
  public EndpointType(String definitionName){
    this.definitionName = definitionName;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((definitionName == null) ? 0 : definitionName.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    EndpointType other = (EndpointType) obj;
    if (definitionName == null) {
      if (other.definitionName != null) {
        return false;
      }
    } else if (!definitionName.equals(other.definitionName)) {
      return false;
    }
    return true;
  }
}
