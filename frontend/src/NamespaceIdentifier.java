package org.whdl.frontend;

import java.util.ArrayList;
import java.util.List;

class NamespaceIdentifier {

  private ArrayList<String> name;

  public NamespaceIdentifier(ArrayList<String> name) {
    this.name = name;
  }

  public List<String> getName() {
    return java.util.Collections.unmodifiableList(name);
  }
}
