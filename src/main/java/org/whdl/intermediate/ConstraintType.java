package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


public class ConstraintType extends Type {
  private Map<String, UserDefinedType> arguments;
  
  public ConstraintType(String typename){
    super(typename);
    this.arguments = new HashMap<String, UserDefinedType>();

  }
  
  @Override
  public Value instantiate() {
    Constraint con = new Constraint(this);
    // elaborate default arguments
    for(Entry<String, UserDefinedType> arg : arguments.entrySet()){
      String argName = arg.getKey();
      UserDefinedType argTypeValue = arg.getValue();
      Value argValue = argTypeValue.instantiate();
      con.setArgument(argName, argValue);
    }
    return con;
  }
  
  public void addArgument(String argName, UserDefinedType argType) {
    // FIXME multiple additions of the same attribute?
    arguments.put(argName, argType);
  }
  
}
