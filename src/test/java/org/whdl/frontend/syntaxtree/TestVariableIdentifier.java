package org.whdl.frontend.syntaxtree;

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
  
  @Test
  public void testEquals(){
	  // an identifier must be equal to itself
	  VariableIdentifier v1 = getInstance();
	  assertEquals(v1, v1);
	  // identifiers that have the same namespace and name must be equal
	  VariableIdentifier v2 = new VariableIdentifier(getNamespaceIdentifierInstance(), "foo");
	  assertEquals(v1, v2);
	  // identifiers that disagree on name cannot be equal
	  VariableIdentifier v3 = new VariableIdentifier(getNamespaceIdentifierInstance(), "bar");
	  assertNotEquals(v1, v3);
	  // identifiers that disagree on namespace cannot be equal
	  ArrayList<String> name = new ArrayList<String>(1);
	  name.add("bogus");
	  NamespaceIdentifier n4 = new NamespaceIdentifier(name);
	  VariableIdentifier v4 = new VariableIdentifier(n4, "foo");
	  assertNotEquals(v1, v4);
  }
}
