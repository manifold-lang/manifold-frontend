package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;

public class Connection {
  private String instanceName;
  private String typename;
  private Map<String, DomainObject> attributes;
  private Endpoint eFrom, eTo;
  
  public Connection(String instanceName, String typename, Endpoint eFrom, Endpoint eTo){
    this.instanceName = instanceName;
    this.typename = typename;
    this.attributes = new HashMap<String, DomainObject>();
    this.eFrom = eFrom;
    this.eTo = eTo;
  }
}
