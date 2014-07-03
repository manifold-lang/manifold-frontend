package org.manifold.intermediate;

import java.util.Map;

import com.google.common.collect.ImmutableMap;


public class ConstraintType extends Type {
  private final ImmutableMap<String, Type> attributes;
  
  public ConstraintType(Map<String, Type> attributes){
    this.attributes = ImmutableMap.copyOf(attributes);
  }
  
  public ImmutableMap<String, Type> getAttributes() {
    return attributes;
  }
}
