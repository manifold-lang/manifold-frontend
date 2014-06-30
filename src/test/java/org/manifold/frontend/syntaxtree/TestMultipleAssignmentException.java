package org.manifold.frontend.syntaxtree;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import org.manifold.frontend.syntaxtree.BitTypeValue;
import org.manifold.frontend.syntaxtree.Expression;
import org.manifold.frontend.syntaxtree.LiteralExpression;
import org.manifold.frontend.syntaxtree.MultipleAssignmentException;
import org.manifold.frontend.syntaxtree.NamespaceIdentifier;
import org.manifold.frontend.syntaxtree.Variable;
import org.manifold.frontend.syntaxtree.VariableIdentifier;

public class TestMultipleAssignmentException {

  private NamespaceIdentifier getNamespaceIdentifierInstance() {
    ArrayList<String> name = new ArrayList<String>(1);
    name.add("whdl");
    return new NamespaceIdentifier(name);
  }

  private VariableIdentifier getVariableIdentifierInstance() {
    return new VariableIdentifier(getNamespaceIdentifierInstance(), "foo");
  }

  private Expression getTypeExpression(){
    return new LiteralExpression(BitTypeValue.getInstance());
  }
  
  public Variable getVariableInstance(){
      return new Variable(getVariableIdentifierInstance(), getTypeExpression());
  }
  
  public MultipleAssignmentException getInstance(){
    return new MultipleAssignmentException(getVariableInstance());
  }
  
  @Test
  public void testGetMessage_containsVariableIdentifier() {
    MultipleAssignmentException instance = getInstance();
    String message = instance.getMessage();
    assertTrue(message.contains(getVariableInstance().getIdentifier().toString()));
  }

}
