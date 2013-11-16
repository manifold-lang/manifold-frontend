package org.whdl.frontend.syntaxtree;

public class VariableIdentifier {

  private String name;
  private NamespaceIdentifier namespaceIdentifier;

  public VariableIdentifier(NamespaceIdentifier namespaceIdentifier,
                            String name) {
    this.name = name;
    this.namespaceIdentifier = namespaceIdentifier;
  }

  public String getName() {
    return name;
  }
  
  public NamespaceIdentifier getNamespaceIdentifier() {
    return namespaceIdentifier;
  }
  
  @Override
  public String toString(){
	  String nsString = getNamespaceIdentifier().toString();
	  if(nsString.isEmpty()){
		  return getName();
	  }else{
		  // FIXME this is syntax-dependent
		  return nsString + "." + getName();
	  }
  }
}
