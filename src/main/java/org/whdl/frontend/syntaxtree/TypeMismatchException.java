package org.whdl.frontend.syntaxtree;

public class TypeMismatchException extends Exception {
  private static final long serialVersionUID = 1L;

  private TypeValue expectedType, actualType;

  public TypeMismatchException(TypeValue expectedType, TypeValue actualType) {
    this.expectedType = expectedType;
    this.actualType = actualType;
  }
  
  public String getMessage() {
    return "type mismatch: expected '" + expectedType + "', actual '" +
      actualType + "'"; 
  }
  
}
