package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;

public class Constraint extends Value {
  private ConstraintTypeDefinition definition;
  public ConstraintTypeDefinition getDefinition(){
    return this.definition;
  }
  
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
  
  public Constraint(String instanceName, ConstraintTypeDefinition definition){
    super(new ConstraintType(definition), instanceName);
    this.definition = definition;
    this.arguments = new HashMap<String, Value>();
  }

}
