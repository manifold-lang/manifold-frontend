package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;

import org.whdl.intermediate.definitions.ConnectionDefinition;
import org.whdl.intermediate.definitions.NodeDefinition;

public class Node extends Value{
  private String instanceName;
  private NodeDefinition definition;
  public NodeDefinition getDefinition(){
    return this.definition;
  }
  
  private Map<String, Value> attributes;
  private Map<String, Endpoint> endpoints;
  
  public Node(String instanceName, NodeDefinition definition){
    this.instanceName = instanceName;
    this.definition = definition;
    this.attributes = new HashMap<String, Value>();
    this.endpoints = new HashMap<String, Endpoint>();
  }

  @Override
  public Type acceptVisitor(ValueTypeVisitor v) {
    return v.visit(this);
  }
}
