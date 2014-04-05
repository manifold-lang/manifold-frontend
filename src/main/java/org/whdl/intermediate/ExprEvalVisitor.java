package org.whdl.intermediate;

public class ExprEvalVisitor {

  // FIXME this is backwards; copy the current ExprTypeVisitor
  
  public Object visit(Expression e){
    return e.accept(this);
  }
  
}
