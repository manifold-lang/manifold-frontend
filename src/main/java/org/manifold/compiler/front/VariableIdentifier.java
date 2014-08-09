package org.manifold.compiler.front;

import java.util.List;

public class VariableIdentifier {

  private String name;
  private NamespaceIdentifier namespaceIdentifier;

  public VariableIdentifier(List<String> identifiers) {
    assert(identifiers.size() > 0);
    this.name = identifiers.get(identifiers.size() - 1);
    this.namespaceIdentifier = new NamespaceIdentifier(
        identifiers.subList(0, identifiers.size() - 1)
    );
  }
  
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
  public String toString() {
    if (getNamespaceIdentifier().isEmpty()) {
      return getName();
    } else {
      return getNamespaceIdentifier().toString() +
          NamespaceIdentifier.getSeparator() + getName();
    }
  }

  @Override
  public int hashCode() {
    return 3 + 19 * getName().hashCode() + 37
        * getNamespaceIdentifier().hashCode();
  }

  @Override
  public boolean equals(Object aThat) {
    if (this == aThat) {
      return true;
    }
    if (!(aThat instanceof VariableIdentifier)) {
      return false;
    }
    VariableIdentifier that = (VariableIdentifier) aThat;
    // two variable identifiers are equal if they have the same namespace
    // and name
    return (this.getName().equals(that.getName()) && this
        .getNamespaceIdentifier().equals(that.getNamespaceIdentifier()));
  }
}
