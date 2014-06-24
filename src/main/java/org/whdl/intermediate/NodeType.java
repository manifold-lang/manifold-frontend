package org.whdl.intermediate;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class NodeType extends Type {

  private Map<String, Type> attributes;
  private Map<String, PortType> ports;

  public NodeType(Map<String, Type> attributes, Map<String, PortType> ports) {
    this.attributes = ImmutableMap.copyOf(attributes);
    this.ports = ImmutableMap.copyOf(ports);
  }
  
  public Map<String, Type> getAttributes() {
    return this.attributes;
  }

  public Map<String, PortType> getPorts() {
    return this.ports;
  }
}
