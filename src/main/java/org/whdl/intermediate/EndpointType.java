package org.whdl.intermediate;


public class EndpointType extends Type {
  private EndpointTypeDefinition definition;
  
  public EndpointType(EndpointTypeDefinition definition){
    this.definition = definition;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((definition == null) ? 0 : definition.hashCode());
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
    if (definition == null) {
      if (other.definition != null) {
        return false;
      }
    } else if (!definition.equals(other.definition)) {
      return false;
    }
    return true;
  }
  
  @Override
  public Value instantiate(){
    // look up the Definition of this EndpointType and instantiate that
    return definition.instantiate();
  }
  
}
