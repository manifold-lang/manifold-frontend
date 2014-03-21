package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;

public class Endpoint {
  private String instanceName;
  private String typename;
  private Map<String, Expression> attributes;
  
  public Endpoint(String instanceName, String typename){
    this.instanceName = instanceName;
    this.typename = typename;
    this.attributes = new HashMap<String, Expression>();
  }
}
