package org.manifold.compiler.front;

import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

public class FunctionTypeValueExpression extends Expression {

  private Expression inputTypeExpr;
  public Expression getInputTypeExpression() {
    return inputTypeExpr;
  }
  
  private Expression outputTypeExpr;
  public Expression getOutputTypeExpression() {
    return outputTypeExpr;
  }
  
  public FunctionTypeValueExpression(
      Expression inputType, Expression outputType) {
    this.inputTypeExpr = inputType;
    this.outputTypeExpr = outputType;
  }

  @Override
  public TypeValue getType(Scope scope) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Value getValue(Scope scope) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void verify(Scope scope) throws Exception {
  }

  @Override
  public boolean isAssignable() {
    return false;
  }

  @Override
  public boolean isElaborationtimeKnowable(Scope scope) {
    return true;
  }

  @Override
  public boolean isRuntimeKnowable(Scope scope) {
    return false;
  }

  @Override
  public void accept(ExpressionVisitor visitor) throws Exception {
    visitor.visit(this);
  }
  
}
