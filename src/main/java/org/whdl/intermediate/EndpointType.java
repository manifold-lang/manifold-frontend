package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


public class EndpointType extends Type {
  private Map<String, Type> attributes;
  
  public EndpointType(Map<String, Type> attributes){
    this.attributes = new HashMap<String, Type>(attributes);
  }
  
}
