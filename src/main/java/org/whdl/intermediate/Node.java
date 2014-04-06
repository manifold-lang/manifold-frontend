package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;

public class Node extends Value{
  private NodeTypeDefinition definition;
  public NodeTypeDefinition getDefinition(){
    return this.definition;
  }
  
  private Map<String, Value> attributes;
  private Map<String, Endpoint> endpoints;
  
  public Node(String instanceName, NodeTypeDefinition definition){
    super(instanceName);
    this.definition = definition;
    this.attributes = new HashMap<String, Value>();
    this.endpoints = new HashMap<String, Endpoint>();
  }

  @Override
  public Type acceptVisitor(ValueTypeVisitor v) {
    return v.visit(this);
  }
}
