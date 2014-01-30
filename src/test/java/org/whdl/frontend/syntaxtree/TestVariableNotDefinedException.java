package org.whdl.frontend.syntaxtree;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class TestVariableNotDefinedException {

  private NamespaceIdentifier namespaceIdentifierInstance;

  private NamespaceIdentifier getNamespaceIdentifierInstance() {
    if (namespaceIdentifierInstance == null) {
      ArrayList<String> name = new ArrayList<String>(1);
      name.add("whdl");

      namespaceIdentifierInstance = new NamespaceIdentifier(name);
    }

    return namespaceIdentifierInstance;
  }

  private VariableIdentifier getVariableIdentifierInstance() {
    return new VariableIdentifier(getNamespaceIdentifierInstance(), "foo");
  }
  
  public VariableNotDefinedException getInstance(){
    return new VariableNotDefinedException(getVariableIdentifierInstance());
  }
  
  @Test
  public void testGetMessage_containsVariableIdentifier() {
    VariableNotDefinedException instance = getInstance();
    String message = instance.getMessage();
    assertTrue(message.contains(getVariableIdentifierInstance().toString()));
  }

}
