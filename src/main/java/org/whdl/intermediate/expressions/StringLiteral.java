package org.whdl.intermediate.expressions;

import org.whdl.intermediate.ExprEvalVisitor;
import org.whdl.intermediate.ExprTypeVisitor;
import org.whdl.intermediate.Expression;
import org.whdl.intermediate.Type;
import org.whdl.intermediate.types.StringType;

public class StringLiteral implements Expression {

  private String val;
  public StringLiteral(String val){
    this.val = val;
  }
  
  @Override
  public Type accept(ExprTypeVisitor v) {
    return new StringType();
  }

  @Override
  public Object accept(ExprEvalVisitor v){
    return val;
  }
  
}
