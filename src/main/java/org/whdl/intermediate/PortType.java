package org.whdl.intermediate;

import java.util.Map;

import com.google.common.collect.ImmutableMap;


public class PortType extends Type {
  private Map<String, Type> attributes;
  
  public PortType(Map<String, Type> attributes){
    this.attributes = ImmutableMap.copyOf(attributes);
  }
  
  public Map<String, Type> getAttributes() {
    return this.attributes;
  }
}
