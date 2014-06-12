package org.whdl.frontend.syntaxtree;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;

public class TestVariableIdentifier {

  private NamespaceIdentifier namespaceIdentifierInstance;

  private NamespaceIdentifier getNamespaceIdentifierInstance() {
    if (namespaceIdentifierInstance == null) {
      namespaceIdentifierInstance = new NamespaceIdentifier("whdl");
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
    assertSame(
      getInstance().getNamespaceIdentifier(),
      getNamespaceIdentifierInstance()
    );
  }
  
  @Test
  public void equals_itself() {
    VariableIdentifier id = getInstance();
    assertEquals(id, id);
  }
  
  @Test
  public void equals_true() {
    VariableIdentifier v = new VariableIdentifier(
        getNamespaceIdentifierInstance(),
        "foo"
    );
    assertEquals(getInstance(), v);
  }
    
  @Test
  public void equals_false_name() {
    VariableIdentifier v = new VariableIdentifier(
        getNamespaceIdentifierInstance(),
        "bar"
    );
    assertNotEquals(getInstance(), v);
  }
  
  @Test
  public void equals_false_typeof() {
    assertNotEquals(getInstance(), "foo");
  }
    
  @Test
  public void equals_false_namespace() {
    ArrayList<String> namespaceName = new ArrayList<String>(1);
    namespaceName.add("bogus");
    
    VariableIdentifier v = new VariableIdentifier(
        new NamespaceIdentifier(namespaceName),
        "foo"
    );
    assertNotEquals(getInstance(), v);
  }
  
  @Test
  public void toString_namespaced() {
    assertEquals(getInstance().toString(), "whdl:foo");
  }
  
  @Test
  public void toString_notNamespaced() {
    VariableIdentifier identifier = new VariableIdentifier(
      new NamespaceIdentifier(new ArrayList()),
      "foo"
    );
    
    assertEquals(identifier.toString(), "foo");
  }
}
