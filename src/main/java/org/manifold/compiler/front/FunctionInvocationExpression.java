package org.manifold.compiler.front;

import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

public class FunctionInvocationExpression extends Expression {
  private Expression functionExpression;
  private Expression inputExpression;

  public FunctionInvocationExpression(
      Expression functionExpression,
      Expression inputExpression) {
    this.functionExpression = functionExpression;
    this.inputExpression = inputExpression;
  }
  
  private FunctionValue getFunctionValue(Scope scope) {
    return functionExpression.evaluate(scope, FunctionValue.class);
  }
  
  private FunctionTypeValue getFunctionTypeValue(Scope scope) {
    return (FunctionTypeValue) getFunctionValue(scope).getType();
  }
  
  @Override
  public TypeValue getType(Scope scope) {
    return getFunctionTypeValue(scope).getOutputType();
  }

  @Override
  public Value evaluate(Scope scope) {
    assert(false);
    return null;
  }

  @Override
  public void verify(Scope scope) throws Exception{
    functionExpression.verify(scope);
    inputExpression.verify(scope);
    
    TypeValue functionInputType = getFunctionTypeValue(scope).getInputType();
    TypeValue inputType = inputExpression.getType(scope);
    
    assert(inputType.isSubtypeOf(functionInputType));
  }

  @Override
  public boolean isAssignable() {
    return false;
  }

  @Override
  public boolean isCompiletimeEvaluable(Scope scope) {
    return false;
  }

  @Override
  public boolean isSynthesizable(Scope scope) {
    return true;
  }

}
