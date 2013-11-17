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
  
  public static String getSeparator(){
	  // FIXME this is syntax-dependent, but at least it abstracts out the dependency
	  return ":";
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
			  sb.append(getSeparator());
		  }
		  sb.append(n);		  
	  }
	  return sb.toString();
  }
  
  @Override
  public int hashCode(){
	  int hash = 1;
	  for(String n : name){
		  hash = hash * 17 + n.hashCode();
	  }
	  return hash;
  }
  
  @Override
  public boolean equals(Object aThat){
	  if(this == aThat) return true;
	  if(!(aThat instanceof NamespaceIdentifier)) return false;
	  NamespaceIdentifier that = (NamespaceIdentifier)aThat;
	  // two namespace identifiers are equal if they name the same namespace
	  return this.getName().equals(that.getName());
  }
}
