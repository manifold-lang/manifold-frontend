package org.manifold.compiler.front;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;
import org.manifold.compiler.BooleanTypeValue;

public class TestMultipleAssignmentException {

  private NamespaceIdentifier getNamespaceIdentifierInstance() {
    ArrayList<String> name = new ArrayList<>(1);
    name.add("whdl");
    return new NamespaceIdentifier(name);
  }

  private VariableIdentifier getVariableIdentifierInstance() {
    return new VariableIdentifier(getNamespaceIdentifierInstance(), "foo");
  }

  private Expression getTypeExpression() {
    return new LiteralExpression(BooleanTypeValue.getInstance());
  }

  public Variable getVariableInstance() {
    return new Variable(
        new Scope(),
        getVariableIdentifierInstance(),
        getTypeExpression()
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
