package org.manifold.compiler.front;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

public class TestVariableNotDefinedException {

  private NamespaceIdentifier getNamespaceIdentifierInstance() {
    ArrayList<String> name = new ArrayList<>(1);
    name.add("manifold");
    return new NamespaceIdentifier(name);
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
