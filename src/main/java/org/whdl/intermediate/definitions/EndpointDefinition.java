package org.whdl.intermediate.definitions;

import java.util.HashMap;
import java.util.Map;

public class EndpointDefinition {
  private String typename;
  public String getTypename(){
    return typename;
  }
  private Map<String, TypeDefinition> attributes;
  
  public EndpointDefinition(String typename){
    this.typename = typename;
    this.attributes = new HashMap<String, TypeDefinition>();
  }
}
