package org.manifold.compiler.front;

public class MultipleAssignmentException extends Exception {
  private static final long serialVersionUID = 1L;

  private final Variable variable;
  
  public MultipleAssignmentException(Variable var) {
    this.variable = var;
  }

  @Override
  public String getMessage() {
    return "multiple assignment to variable '" + variable.getIdentifier() + "'";
  }
}
