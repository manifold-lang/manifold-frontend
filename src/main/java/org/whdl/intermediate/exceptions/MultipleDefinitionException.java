package org.whdl.intermediate.exceptions;

public class MultipleDefinitionException extends Exception {
  private static final long serialVersionUID = -5366240749138487226L;

  private String typename;
  public MultipleDefinitionException(String typename){
    this.typename = typename;
  }
  @Override
  public String getMessage(){
    return "multiple definitions of '" + typename + "'";
  }
}
