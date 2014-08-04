package org.manifold.compiler.front;

import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

public class VariableAssignmentExpression extends Expression {
  private Variable variable;
  private Expression valueExpression;

  public VariableAssignmentExpression(Variable variable, Expression valueExpression) throws Exception {
    this.variable = variable;
    this.valueExpression = valueExpression;
    this.variable.setValueExpression(valueExpression);
  }

  @Override
  public TypeValue getType() {
    return variable.getType();
  }

  @Override
  public Value evaluate() {
    return variable.getValue();
  }

  @Override
  public void verify() throws Exception{
    variable.verify();
    valueExpression.verify();
  }

  @Override
  public boolean isAssignable() {
    return valueExpression.isAssignable();
  }

  @Override
  public boolean isCompiletimeEvaluable() {
    return valueExpression.evaluate().isElaborationtimeKnowable();
  }

  @Override
  public boolean isSynthesizable() {
    return valueExpression.evaluate().isRuntimeKnowable();
  }

}
