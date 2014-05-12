package org.whdl.intermediate;

public abstract class TypeDefinition {
  private String typename;
  public String getTypename() {
    return typename;
  }
  
  public TypeDefinition(String typename){
    this.typename = typename;
  }
  
  public abstract Value instantiate(String instanceName);
}
