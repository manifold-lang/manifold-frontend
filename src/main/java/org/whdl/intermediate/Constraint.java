package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;

import org.whdl.intermediate.definitions.ConnectionDefinition;
import org.whdl.intermediate.definitions.ConstraintDefinition;

public class Constraint extends Value {
  private String instanceName;
  private ConstraintDefinition definition;
  public ConstraintDefinition getDefinition(){
    return this.definition;
  }
  
  private Map<String, Value> arguments;  
  
  public Constraint(String instanceName, ConstraintDefinition definition){
    this.instanceName = instanceName;
    this.definition = definition;
    this.arguments = new HashMap<String, Value>();
  }

  @Override
  public Type acceptVisitor(ValueTypeVisitor v) {
    return v.visit(this);
  }
}
