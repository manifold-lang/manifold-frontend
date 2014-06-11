package org.whdl.intermediate;

public class MultipleInstantiationException extends Exception {

  private static final long serialVersionUID = 5454612285640527276L;
  private String kind;
  private String instanceName;
  
  public MultipleInstantiationException(String kind, String instanceName){
    this.kind = kind;
    this.instanceName = instanceName;
  }
  
  @Override
  public String getMessage(){
    return "multiple instantiations of " + kind + " '" + instanceName + "'";
  }
  
}
