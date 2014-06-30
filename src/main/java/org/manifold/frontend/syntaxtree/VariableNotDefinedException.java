package org.manifold.frontend.syntaxtree;

public class VariableNotDefinedException extends Exception {
  private static final long serialVersionUID = 1L;

  private VariableIdentifier identifier;
  public VariableNotDefinedException(VariableIdentifier id){
    this.identifier = id;
  }
  @Override
  public String getMessage(){
    return "variable '" + identifier + "' not defined in this scope";
  }
}
