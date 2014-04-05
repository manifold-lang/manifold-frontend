package org.whdl.intermediate;

import org.whdl.intermediate.types.PrimitiveType;

public class IntegerLiteral extends Expression {

  private Integer val;
  public IntegerLiteral(Integer val){
    this.val = val;
  }
  
  @Override
  public Type accept(ExprTypeVisitor v){
    return v.visit(this);
  }

  @Override
  public Object accept(ExprEvalVisitor v){
    return val;
  }
  
}
