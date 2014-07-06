package org.manifold.compiler;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class NodeType extends Type {

  private final Map<String, Type> attributes;
  private final Map<String, PortType> ports;

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
