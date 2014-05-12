package org.whdl.intermediate;


public class NodeType extends Type {
  private NodeTypeDefinition definition;
  
  public NodeType(NodeTypeDefinition definition){
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
    NodeType other = (NodeType) obj;
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
  public Value instantiate(String instanceName){
    // look up the Definition of this NodeType and instantiate that
    return definition.instantiate(instanceName);
  }
  
}
