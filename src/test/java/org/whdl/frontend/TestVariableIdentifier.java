package org.whdl.frontend;

import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.UnsupportedOperationException;
import java.util.ArrayList;

public class TestVariableIdentifier {

  private NamespaceIdentifier namespaceIdentifierInstance;

  private NamespaceIdentifier getNamespaceIdentifierInstance() {
    if (namespaceIdentifierInstance == null) {
      ArrayList<String> name = new ArrayList<String>(1);
      name.add("whdl");

      namespaceIdentifierInstance = new NamespaceIdentifier(name);
    }

    return namespaceIdentifierInstance;
  }

  private VariableIdentifier getInstance() {
    return new VariableIdentifier(getNamespaceIdentifierInstance(), "foo");
  }

  @Test
  public void testGetName() {
    assertEquals(getInstance().getName(), "foo");
  }

  @Test
  public void testGetNamespaceIdentifier() {
    assertSame(getInstance().getNamespaceIdentifier(),
               getNamespaceIdentifierInstance());
  }
}
