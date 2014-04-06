package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;

import org.whdl.intermediate.definitions.ConnectionDefinition;
import org.whdl.intermediate.definitions.EndpointDefinition;

public class Endpoint extends DomainObject {
  private String instanceName;
  private EndpointDefinition definition;
  public EndpointDefinition getDefinition(){
    return this.definition;
  }
  
  private Map<String, DomainObject> attributes;
  private Node parent;
  
  public Endpoint(String instanceName, EndpointDefinition definition, Node parent){
    this.instanceName = instanceName;
    this.definition = definition;
    this.attributes = new HashMap<String, DomainObject>();
    this.parent = parent;
  }

  @Override
  public Type acceptVisitor(DomainObjectTypeVisitor v) {
    return v.visit(this);
  }
}
