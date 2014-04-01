package org.whdl.intermediate.expressions;

import org.whdl.intermediate.ExprEvalVisitor;
import org.whdl.intermediate.ExprTypeVisitor;
import org.whdl.intermediate.Expression;
import org.whdl.intermediate.Type;
import org.whdl.intermediate.types.PrimitiveType;

public class BooleanLiteral implements Expression {

  private Boolean val;
  public BooleanLiteral(Boolean val){
    this.val = val;
  }
  
  @Override
  public Type accept(ExprTypeVisitor v) {
    return PrimitiveType.BOOLEAN;
  }
  
  @Override
  public Object accept(ExprEvalVisitor v){
    return val;
  }

}
