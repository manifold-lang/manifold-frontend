package org.manifold.compiler.front;

import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

public class TypeMismatchException extends Exception {
  private static final long serialVersionUID = 1L;

  private final TypeValue expectedType;
  private final Value actualType;

  public TypeMismatchException(TypeValue expectedType, Value actualType) {
    this.expectedType = expectedType;
    this.actualType = actualType;
  }
  
  @Override
  public String getMessage() {
    return "type mismatch: expected '" + expectedType + "', actual '" +
      actualType + "'"; 
  }
  
}
