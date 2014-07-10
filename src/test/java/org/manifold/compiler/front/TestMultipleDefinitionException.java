package org.manifold.compiler.front;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;

public class TestMultipleDefinitionException {

  private NamespaceIdentifier getNamespaceIdentifierInstance() {
    ArrayList<String> name = new ArrayList<>(1);
    name.add("whdl");
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
