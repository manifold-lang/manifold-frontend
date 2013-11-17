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
  public void testEqualsItself() {
    assertEquals(getInstance(), getInstance());
  }
  
  @Test
  public void testEquals() {
    VariableIdentifier v = new VariableIdentifier(
        getNamespaceIdentifierInstance(),
        "foo"
    );
    assertEquals(getInstance(), v);
  }
    
  @Test
  public void testNotEqualsName() {
    VariableIdentifier v = new VariableIdentifier(
        getNamespaceIdentifierInstance(),
        "bar"
    );
    assertNotEquals(getInstance(), v);
  }
    
  @Test
  public void testNotEqualsNamespace() {
    ArrayList<String> namespaceName = new ArrayList<String>(1);
    namespaceName.add("bogus");
    
    VariableIdentifier v = new VariableIdentifier(
        new NamespaceIdentifier(namespaceName),
        "foo"
    );
    assertNotEquals(getInstance(), v);
  }
}
