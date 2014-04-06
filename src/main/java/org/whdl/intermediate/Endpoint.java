package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;

public class Endpoint extends Value {
  private EndpointTypeDefinition definition;
  public EndpointTypeDefinition getDefinition(){
    return this.definition;
  }
  
  private Map<String, Value> attributes;
  private Node parent;
  
  public Endpoint(String instanceName, EndpointTypeDefinition definition, Node parent){
    super(instanceName);
    this.definition = definition;
    this.attributes = new HashMap<String, Value>();
    this.parent = parent;
  }

  @Override
  public Type acceptVisitor(ValueTypeVisitor v) {
    return v.visit(this);
  }
}
