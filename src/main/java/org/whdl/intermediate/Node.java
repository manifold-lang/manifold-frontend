package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;

public class Node {
  private String instanceName;
  private String typename;
  private Map<String, Expression> attributes;
  private Map<String, Endpoint> endpoints;
  
  public Node(String instanceName, String typename){
    this.instanceName = instanceName;
    this.typename = typename;
    this.attributes = new HashMap<String, Expression>();
    this.endpoints = new HashMap<String, Endpoint>();
  }
}
