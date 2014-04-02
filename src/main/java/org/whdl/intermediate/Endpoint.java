package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;

public class Endpoint {
  private String instanceName;
  private String typename;
  private Map<String, Expression> attributes;
  private Node parent;
  
  public Endpoint(String instanceName, String typename, Node parent){
    this.instanceName = instanceName;
    this.typename = typename;
    this.attributes = new HashMap<String, Expression>();
    this.parent = parent;
  }
}
