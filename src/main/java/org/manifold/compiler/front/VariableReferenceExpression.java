package org.manifold.compiler.front;

import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

public class VariableReferenceExpression extends Expression {
  final private Variable variable;

  public VariableReferenceExpression(Variable variable) {
    this.variable = variable;
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
  public void verify() throws Exception {
    variable.verify();
    if (!variable.isAssigned()) {
      throw new VariableNotAssignedException(variable);
    }
  }

  @Override
  public boolean isAssignable() {
    return true;
  }

  @Override
  public boolean isCompiletimeEvaluable() {
    return variable.getValue().isCompiletimeEvaluable();
  }

  @Override
  public boolean isSynthesizable() {
    return variable.getValue().isSynthesizable();
  }

}
