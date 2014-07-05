package org.manifold.intermediate;

public class MultipleDefinitionException extends SchematicException {
  
  private static final long serialVersionUID = -5366240749138487225L;
  private final String kind;
  private final String typename;
  
  public MultipleDefinitionException(String kind, String typename){
    this.kind = kind;
    this.typename = typename;
  }
  @Override
  public String getMessage(){
    return "multiple definitions of " + kind +  " '" + typename + "'";
  }

}
