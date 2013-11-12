package org.whdl.frontend;

import java.util.ArrayList;

class NamespaceIdentifier {

  private List<String> name;

  public NamespaceIdentifier(List<String> name) {
    this.name = ava.util.Collections.unmodifiableList(name);
  }

  public List<String> getName() {
    return name;
  }
}
