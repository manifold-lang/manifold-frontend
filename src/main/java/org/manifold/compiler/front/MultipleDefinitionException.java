package org.manifold.compiler.front;

public class MultipleDefinitionException extends Exception {
  private static final long serialVersionUID = 1L;
  
  private VariableIdentifier identifier;

  public MultipleDefinitionException(VariableIdentifier id) {
    this.identifier = id;
  }

  @Override
  public String getMessage(){
    return "multiple definitions of variable '" + identifier + "'";
  }

}
