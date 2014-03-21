package org.whdl.intermediate;

public class ExprTypeVisitor {

  public Type visit(Expression e) {
    return e.accept(this);
  }

}
