package org.whdl.intermediate.exceptions;

public class MultipleDefinitionException extends Exception {
  private static final long serialVersionUID = -5366240749138487225L;

  private String kind;
  private String typename;
  public MultipleDefinitionException(String kind, String typename){
    this.kind = kind;
    this.typename = typename;
  }
  @Override
  public String getMessage(){
    return "multiple definitions of " + kind +  "'" + typename + "'";
  }
}
