package org.manifold.compiler.front;

import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

public class VariableAssignmentExpression extends Expression {
  private VariableIdentifier variable;
  private final Expression lvalueExpression;
  private final Expression rvalueExpression;
  
  public Expression getLvalueExpression() {
    return lvalueExpression;
  }
  
  public Expression getRvalueExpression() {
    return rvalueExpression;
  }

  public VariableAssignmentExpression(
      Expression lvalueExpression,
      Expression rvalueExpression) {
    this.lvalueExpression = lvalueExpression;
    this.rvalueExpression = rvalueExpression;
  }

  @Override
  public TypeValue getType(Scope scope) {
    return lvalueExpression.getType(scope);
  }

  @Override
  public Value getValue(Scope scope) {
    return lvalueExpression.getValue(scope);
  }

  @Override
  public void verify(Scope scope) throws Exception{
    lvalueExpression.verify(scope);
    rvalueExpression.verify(scope);
    
    TypeValue rvalueType = rvalueExpression.getType(scope);
    TypeValue lvalueType = lvalueExpression.getType(scope);
    
    assert(lvalueType.isSubtypeOf(rvalueType));
    assert(lvalueExpression.isAssignable());
  }

  @Override
  public boolean isAssignable() {
    return rvalueExpression.isAssignable();
  }

  @Override
  public boolean isElaborationtimeKnowable(Scope scope) {
    return rvalueExpression.isElaborationtimeKnowable(scope);
  }

  @Override
  public boolean isRuntimeKnowable(Scope scope) {
    return rvalueExpression.isRuntimeKnowable(scope);
  }

  @Override
  public void accept(ExpressionVisitor visitor) {
    visitor.visit(this);
  }
  
}
