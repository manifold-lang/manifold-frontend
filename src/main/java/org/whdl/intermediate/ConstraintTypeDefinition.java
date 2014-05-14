package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ConstraintTypeDefinition extends TypeDefinition {
  private Map<String, TypeTypeDefinition> arguments;
  
  public ConstraintTypeDefinition(String typename){
    super(typename);
    this.arguments = new HashMap<String, TypeTypeDefinition>();
  }
  
  @Override
  public Value instantiate(String instanceName) {
    Constraint con = new Constraint(instanceName, this);
    // elaborate default arguments
    for(Entry<String, TypeTypeDefinition> arg : arguments.entrySet()){
      String argName = arg.getKey();
      TypeTypeDefinition argTypeValue = arg.getValue();
      Value argValue = argTypeValue.instantiate(argName);
      con.setArgument(argName, argValue);
    }
    return con;
  }
  
  public void addArgument(String argName, TypeTypeDefinition argType) {
    // FIXME multiple additions of the same attribute?
    arguments.put(argName, argType);
  }
  
}
