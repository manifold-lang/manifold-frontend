package org.manifold.compiler.front;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NamespaceIdentifier {

  public static String getSeparator(){
    // TODO this is syntax-dependent, but at least it abstracts out the
    // dependency
    return "::";
  }

  private final List<String> name;

  /**
   * Clean up a name by performing the following operations:
   *  - Remove whitespace from each element
   *  - Suppress elements that are equal to the empty string
   */
  private List<String> sanitizeName(List<String> n) {
    List<String> retval = new ArrayList<String>(n.size());
    for (String s : n) {
      s = s.trim();
      if (!(s.isEmpty())) {
        retval.add(s);
      }
    }
    return java.util.Collections.unmodifiableList(retval);
  }

  public NamespaceIdentifier(String name) {
    this.name = sanitizeName(Arrays.asList(name.split(getSeparator())));
  }

  public NamespaceIdentifier(List<String> name) {
    this.name = sanitizeName(name);
  }

  public List<String> getName() {
    return name;
  }

  @Override
  public String toString(){
    // TODO this is syntax-dependent
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (String n : name){
      if (first){
        first = false;
      } else {
        sb.append(getSeparator());
      }
      sb.append(n);
    }
    return sb.toString();
  }

  @Override
  public int hashCode(){
    int hash = 1;
    for (String n : name){
      hash = hash * 17 + n.hashCode();
    }
    return hash;
  }

  @Override
  public boolean equals(Object aThat){
    if (this == aThat) {
      return true;
    } else if (!(aThat instanceof NamespaceIdentifier)) {
      return false;
    } else {
      NamespaceIdentifier that = (NamespaceIdentifier) aThat;
      // two namespace identifiers are equal if they name the same namespace
      return this.getName().equals(that.getName());
    }
  }

  public boolean isEmpty() {
    return this.name.isEmpty();
  }
}
