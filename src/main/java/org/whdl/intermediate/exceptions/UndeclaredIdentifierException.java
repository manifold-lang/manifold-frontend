package org.whdl.intermediate.exceptions;

public class UndeclaredIdentifierException extends Exception {
  private static final long serialVersionUID = -5785755001929744865L;
  public String identifier;
  
  public UndeclaredIdentifierException(String identifier){
    this.identifier = identifier;
  }
  
  @Override
  public String getMessage(){
    return "undeclared identifier '" + this.identifier + "'";
  }
}
