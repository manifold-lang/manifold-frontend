package org.whdl.frontend;

class NamespaceIdentifier {

  private ArrayList<String> name;

  public NamespaceIdentifier(ArrayList<String> name) {
    this.name = name;
  }

  public ArrayList<String> getName() {
    return java.util.Collections.unmodifiableList(Array.asList(name));
  }
}
