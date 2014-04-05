package org.whdl.intermediate;

import org.whdl.intermediate.types.PrimitiveType;

public class BooleanLiteral extends Expression {

  private Boolean val;
  public BooleanLiteral(Boolean val){
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
