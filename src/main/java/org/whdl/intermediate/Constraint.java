package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;

public class Constraint extends Value {
 
  private Map<String, Value> arguments;
  public Value getArgument(String argName) throws UndeclaredIdentifierException{
    if(arguments.containsKey(argName)){
      return arguments.get(argName);
    }else{
      throw new UndeclaredIdentifierException("no argument named '" + argName + "'");
    }
  }
  public void setArgument(String argName, Value argValue){
    arguments.put(argName, argValue);
  }
  
  public Constraint(ConstraintType type){
    super(type);
    this.arguments = new HashMap<String, Value>();
  }

}
