package org.whdl.frontend;

class VariableIdentifier {

  private String name;
  private NamespaceIdentifier namespaceIdentifier;

  public VariableIdentifier(NamespaceIdentifier namespaceIdentifier, String name) {
    this.name = name;
    this.namespaceIdentifier = namespaceIdentifier;
  }

  public String getName() {
    return name;
  }

  public NamespaceIdentifier getNamespaceIdentifier() {
    return namespaceIdentifier;
  }
}