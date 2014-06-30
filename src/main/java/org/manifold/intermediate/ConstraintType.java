package org.manifold.intermediate;

import java.util.HashMap;
import java.util.Map;


public class ConstraintType extends Type {
  private Map<String, Type> arguments;
  
  public ConstraintType(Map<String, Type> arguments){
    this.arguments = new HashMap<String, Type>(arguments);

  }
  
}
