package org.whdl.intermediate.definitions;

import java.util.HashMap;
import java.util.Map;

import org.whdl.intermediate.Expression;

public class ConstraintDefinition {
  private String typename;
  public String getTypename(){
    return typename;
  }
  private Map<String, TypeDefinition> arguments;
  private Expression constraintExpr;
  
  public ConstraintDefinition(String typename, Expression constraintExpr){
    this.typename = typename;
    this.arguments = new HashMap<String, TypeDefinition>();
    this.constraintExpr = constraintExpr; // FIXME how/when to verify correctness of this expr when argument names are specified later?
  }
}
