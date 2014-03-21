package org.whdl.intermediate;

public class ExprEvalVisitor {

  public Object visit(Expression e){
    return e.accept(this);
  }
  
}
