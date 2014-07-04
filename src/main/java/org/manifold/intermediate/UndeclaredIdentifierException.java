package org.manifold.intermediate;

public class UndeclaredIdentifierException extends SchematicException {
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
