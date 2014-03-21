package org.whdl.intermediate.expressions;

import org.whdl.intermediate.ExprEvalVisitor;
import org.whdl.intermediate.ExprTypeVisitor;
import org.whdl.intermediate.Expression;
import org.whdl.intermediate.Type;
import org.whdl.intermediate.types.BooleanType;

public class BooleanLiteral implements Expression {

  private Boolean val;
  public BooleanLiteral(Boolean val){
    this.val = val;
  }
  
  @Override
  public Type accept(ExprTypeVisitor v) {
    return new BooleanType();
  }
  
  @Override
  public Object accept(ExprEvalVisitor v){
    return val;
  }

}
