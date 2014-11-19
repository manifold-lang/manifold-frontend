package org.manifold.compiler.front;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

public class TestMultipleDefinitionException {

  private NamespaceIdentifier getNamespaceIdentifierInstance() {
    ArrayList<String> name = new ArrayList<>(1);
    name.add("manifold");
    return new NamespaceIdentifier(name);
  }

  private VariableIdentifier getVariableIdentifierInstance() {
    return new VariableIdentifier(getNamespaceIdentifierInstance(), "foo");
  }

  public MultipleDefinitionException getInstance(){
    return new MultipleDefinitionException(getVariableIdentifierInstance());
  }

  @Test
  public void testGetMessage_containsVariableIdentifier() {
    MultipleDefinitionException instance = getInstance();
    String message = instance.getMessage();
    assertTrue(message.contains(getVariableIdentifierInstance().toString()));
  }

}
