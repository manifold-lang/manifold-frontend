package org.manifold.compiler.front;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

public class VariableReferenceExpression extends Expression {
  private final VariableIdentifier variable;

  public VariableReferenceExpression(VariableIdentifier variable) {
    this.variable = variable;
  }

  @Override
  public TypeValue getType(Scope scope) {
    return null;
    //return variable.getType();
  }

  @Override
  public Value evaluate(Scope scope) {
    return null;
    //return variable.getValue();
  }

  @Override
  public void verify(Scope scope) throws Exception {
    // variable.verify();
    // if (!variable.isAssigned()) {
    //   throw new VariableNotAssignedException(variable);
    // }
  }

  @Override
  public boolean isAssignable() {
    return true;
  }

  @Override
  public boolean isCompiletimeEvaluable(Scope scope) {
    try {
      return scope.getVariable(variable).getValue().isElaborationtimeKnowable();
    } catch (VariableNotDefinedException ex) {
      assert(false);
      return false;
    }
  }

  @Override
  public boolean isSynthesizable(Scope scope) {
    try {
      return scope.getVariable(variable).getValue().isRuntimeKnowable();
    } catch (VariableNotDefinedException ex) {
      assert(false);
      return false;
    }
  }

}
