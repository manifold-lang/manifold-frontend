package org.manifold.intermediate;

import java.util.HashMap;
import java.util.Map;


public class ConstraintType extends Type {
  private final Map<String, Type> arguments;
  
  public ConstraintType(Map<String, Type> arguments){
    this.arguments = new HashMap<>(arguments);

  }
  
}
