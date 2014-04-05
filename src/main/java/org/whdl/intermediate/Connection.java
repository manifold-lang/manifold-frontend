package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;

import org.whdl.intermediate.definitions.ConnectionDefinition;

public class Connection extends DomainObject {
  private String instanceName;
  private ConnectionDefinition definition;
  public ConnectionDefinition getDefinition(){
    return this.definition;
  }
  
  private Map<String, DomainObject> attributes;
  private Endpoint eFrom, eTo;
  
  public Connection(String instanceName, ConnectionDefinition definition, Endpoint eFrom, Endpoint eTo){
    this.instanceName = instanceName;
    this.definition = definition;
    this.attributes = new HashMap<String, DomainObject>();
    this.eFrom = eFrom;
    this.eTo = eTo;
  }

  @Override
  public Type accept(DomainObjectTypeVisitor v) {
    return v.visit(this);
  }
}
