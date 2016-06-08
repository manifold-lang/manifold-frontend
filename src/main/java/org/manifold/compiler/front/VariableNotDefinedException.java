package org.manifold.compiler.front;

public class VariableNotDefinedException extends FrontendBuildException {
  private static final long serialVersionUID = 1L;

  public VariableNotDefinedException(VariableIdentifier id){
    super("variable '" + id + "' not defined in this scope");
  }
}
