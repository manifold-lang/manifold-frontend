package org.whdl.frontend.syntaxtree;

import java.util.List;

public class NamespaceIdentifier {

  private List<String> name;

  public NamespaceIdentifier(List<String> name) {
    this.name = java.util.Collections.unmodifiableList(name);
  }

  public List<String> getName() {
    return name;
  }
  
  @Override
  public String toString(){
	  // FIXME this is syntax-dependent
	  StringBuilder sb = new StringBuilder();
	  boolean first = true;
	  for(String n : name){
		  if(first){
			  first = false;
		  }else{
			  sb.append(":");
		  }
		  sb.append(n);		  
	  }
	  return sb.toString();
  }
}
