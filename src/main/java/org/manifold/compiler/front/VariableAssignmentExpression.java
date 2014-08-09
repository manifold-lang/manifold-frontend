package org.manifold.compiler.front;

import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

public class VariableAssignmentExpression extends Expression {
  private VariableIdentifier variable;
  private Expression valueExpression;

  public VariableAssignmentExpression(
      VariableIdentifier variable,
      Expression valueExpression) throws Exception {
    this.variable = variable;
    this.valueExpression = valueExpression;
  }

  @Override
  public TypeValue getType(Scope scope) {
    try {
      return scope.getVariable(variable).getType();
    } catch (VariableNotDefinedException ex) {
      assert(false);
      return null;
    }
  }

  @Override
  public Value evaluate(Scope scope) {
    try {
      return scope.getVariable(variable).getValue();
    } catch (VariableNotDefinedException ex) {
      assert(false);
      return null;
    }
  }

  @Override
  public void verify(Scope scope) throws Exception{
    valueExpression.verify(scope);
  }

  @Override
  public boolean isAssignable() {
    return valueExpression.isAssignable();
  }

  @Override
  public boolean isCompiletimeEvaluable(Scope scope) {
    return valueExpression.evaluate(scope).isElaborationtimeKnowable();
  }

  @Override
  public boolean isSynthesizable(Scope scope) {
    return valueExpression.evaluate(scope).isRuntimeKnowable();
  }

}
