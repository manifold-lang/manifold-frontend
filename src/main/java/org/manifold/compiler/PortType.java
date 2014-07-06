package org.manifold.compiler;

import com.google.common.collect.ImmutableMap;
import java.util.Map;

public class PortType extends Type {
  private final Map<String, Type> attributes;
  
  public PortType(Map<String, Type> attributes){
    this.attributes = ImmutableMap.copyOf(attributes);
  }
  
  public Map<String, Type> getAttributes() {
    return this.attributes;
  }
}
