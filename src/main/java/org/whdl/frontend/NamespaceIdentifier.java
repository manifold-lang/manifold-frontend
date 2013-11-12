package org.whdl.frontend;

import java.util.List;

class NamespaceIdentifier {

  private List<String> name;

  public NamespaceIdentifier(List<String> name) {
    this.name = java.util.Collections.unmodifiableList(name);
  }

  public List<String> getName() {
    return name;
  }
}
