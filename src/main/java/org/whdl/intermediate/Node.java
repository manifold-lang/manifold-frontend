package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;

public class Node {
  private String instanceName;
  private String typename;
  private Map<String, DomainObject> attributes;
  private Map<String, Endpoint> endpoints;
  
  public Node(String instanceName, String typename){
    this.instanceName = instanceName;
    this.typename = typename;
    this.attributes = new HashMap<String, DomainObject>();
    this.endpoints = new HashMap<String, Endpoint>();
  }
}
