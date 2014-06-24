package org.manifold.intermediate;

import java.util.HashMap;
import java.util.Map;


public class PortType extends Type {
  private Map<String, Type> attributes;
  
  public PortType(Map<String, Type> attributes){
    this.attributes = new HashMap<String, Type>(attributes);
  }
  
}
