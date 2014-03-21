package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;

public class Constraint {
  private String instanceName;
  private String typename;
  private Map<String, Expression> arguments;
  private Expression constraintExpr;
  
  public Constraint(String instanceName, String typename, Expression expr){
    this.instanceName = instanceName;
    this.typename = typename;
    this.arguments = new HashMap<String, Expression>();
    this.constraintExpr = expr;
  }
}
