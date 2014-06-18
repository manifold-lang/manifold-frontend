package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;


public class ConnectionType extends Type {
  private Map<String, Type> attributes;
  
  public ConnectionType(Map<String, Type> attributes){
    this.attributes = new HashMap<String, Type>(attributes);
  }
  
}
