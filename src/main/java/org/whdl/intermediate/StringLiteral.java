package org.whdl.intermediate;

import org.whdl.intermediate.types.PrimitiveType;

public class StringLiteral extends Expression {

  private String val;
  public StringLiteral(String val){
    this.val = val;
  }
  
  @Override
  public Type accept(ExprTypeVisitor v) {
    return v.visit(this);
  }

  @Override
  public Object accept(ExprEvalVisitor v){
    return val;
  }
  
}
