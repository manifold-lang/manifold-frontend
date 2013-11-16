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
	  List<String> thisName = this.getName();
	  List<String> thatName = that.getName();
	  // two namespace identifiers are equal if they name the same namespace
	  if(thisName.size() != thatName.size()) return false;
	  for(int i = 0; i < thisName.size(); ++i){
		  String nThis = thisName.get(i);
		  String nThat = thatName.get(i);
		  if(!nThis.equals(nThat)) return false;
	  }
	  return true;
  }
}
