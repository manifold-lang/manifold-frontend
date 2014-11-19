package org.manifold.compiler.front;

public class IllegalAssignmentException extends Exception {
  private static final long serialVersionUID = -9111554599011707945L;

  private final Expression lvalue;

  public IllegalAssignmentException(Expression lvalue) {
    this.lvalue = lvalue;
  }

  @Override
  public String getMessage() {
    return "expression '" + lvalue.toString() + "' cannot appear on the"
        + " left-hand side of a variable assignment";
  }

}
