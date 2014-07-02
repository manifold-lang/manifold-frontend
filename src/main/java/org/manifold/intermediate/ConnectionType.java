package org.manifold.intermediate;

import java.util.HashMap;
import java.util.Map;


public class ConnectionType extends Type {
  private final Map<String, Type> attributes;
  
  public ConnectionType(Map<String, Type> attributes){
    this.attributes = new HashMap<>(attributes);
  }
  
}
