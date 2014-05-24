package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


public class ConstraintType extends Type {
  private Map<String, Type> arguments;
  
  public ConstraintType(Map<String, Type> arguments){
    this.arguments = new HashMap<String, Type>(arguments);

  }
  
}
