package org.manifold.intermediate;

public class MultipleAssignmentException extends SchematicException {

  private static final long serialVersionUID = 5454612285640527276L;
  private final String instanceKind;
  private final String instanceName;
  
  public MultipleAssignmentException(String instanceKind, String instanceName) {
    this.instanceKind = instanceKind;
    this.instanceName = instanceName;
  }
  
  @Override
  public String getMessage(){
    return "multiple instantiations of " + instanceKind + " '" +
        instanceName + "'";
  }
  
}
