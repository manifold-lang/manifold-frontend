package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;

import org.whdl.intermediate.definitions.ConnectionDefinition;
import org.whdl.intermediate.definitions.ConstraintDefinition;

public class Constraint extends DomainObject {
  private String instanceName;
  private ConstraintDefinition definition;
  public ConstraintDefinition getDefinition(){
    return this.definition;
  }
  
  private Map<String, DomainObject> arguments;  
  
  public Constraint(String instanceName, ConstraintDefinition definition){
    this.instanceName = instanceName;
    this.definition = definition;
    this.arguments = new HashMap<String, DomainObject>();
  }

  @Override
  public Type acceptVisitor(DomainObjectTypeVisitor v) {
    return v.visit(this);
  }
}
