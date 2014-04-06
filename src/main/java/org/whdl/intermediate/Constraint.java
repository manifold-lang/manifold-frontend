package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;

public class Constraint extends Value {
  private ConstraintTypeDefinition definition;
  public ConstraintTypeDefinition getDefinition(){
    return this.definition;
  }
  
  private Map<String, Value> arguments;  
  
  public Constraint(String instanceName, ConstraintTypeDefinition definition){
    super(instanceName);
    this.definition = definition;
    this.arguments = new HashMap<String, Value>();
  }

  @Override
  public Type acceptVisitor(ValueTypeVisitor v) {
    return v.visit(this);
  }
}
