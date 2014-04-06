package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;

import org.whdl.intermediate.definitions.ConnectionDefinition;
import org.whdl.intermediate.definitions.NodeDefinition;

public class Node extends DomainObject{
  private String instanceName;
  private NodeDefinition definition;
  public NodeDefinition getDefinition(){
    return this.definition;
  }
  
  private Map<String, DomainObject> attributes;
  private Map<String, Endpoint> endpoints;
  
  public Node(String instanceName, NodeDefinition definition){
    this.instanceName = instanceName;
    this.definition = definition;
    this.attributes = new HashMap<String, DomainObject>();
    this.endpoints = new HashMap<String, Endpoint>();
  }

  @Override
  public Type acceptVisitor(DomainObjectTypeVisitor v) {
    return v.visit(this);
  }
}
