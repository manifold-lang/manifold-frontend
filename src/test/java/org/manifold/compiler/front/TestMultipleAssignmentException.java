package org.manifold.compiler.front;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

public class TestMultipleAssignmentException {

  private NamespaceIdentifier getNamespaceIdentifierInstance() {
    ArrayList<String> name = new ArrayList<>(1);
    name.add("manifold");
    return new NamespaceIdentifier(name);
  }

  private VariableIdentifier getVariableIdentifierInstance() {
    return new VariableIdentifier(getNamespaceIdentifierInstance(), "foo");
  }

  public Variable getVariableInstance() {
    return new Variable(
        new Scope(),
        getVariableIdentifierInstance()
    );
  }

  public MultipleAssignmentException getInstance() {
    return new MultipleAssignmentException(getVariableInstance());
  }

  @Test
  public void testGetMessage_containsVariableIdentifier() {
    MultipleAssignmentException instance = getInstance();

    String message = instance.getMessage();
    assertTrue(message.contains(
        getVariableInstance().getIdentifier().toString()
    ));
  }

}
