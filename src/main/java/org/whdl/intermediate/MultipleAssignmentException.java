package org.whdl.intermediate;

public class MultipleAssignmentException extends Exception {

  private static final long serialVersionUID = 5454612285640527276L;
  private String instanceKind;
  private String instanceName;
  
  public MultipleAssignmentException(String instanceKind, String instanceName){
    this.instanceKind = instanceKind;
    this.instanceName = instanceName;
  }
  
  @Override
  public String getMessage(){
    return "multiple instantiations of " + instanceKind + " '" + instanceName + "'";
  }
  
}
