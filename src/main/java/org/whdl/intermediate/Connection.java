package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;

public class Connection {
  private String instanceName;
  private String typename;
  private Map<String, Expression> attributes;
  // FIXME reference to "from" and "to" connection point
  
  public Connection(String instanceName, String typename){
    this.instanceName = instanceName;
    this.typename = typename;
    this.attributes = new HashMap<String, Expression>();
  }
}
