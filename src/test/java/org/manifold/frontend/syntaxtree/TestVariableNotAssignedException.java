package org.manifold.frontend.syntaxtree;

import static org.junit.Assert.*;

import org.junit.Test;
import java.util.ArrayList;

public class TestVariableNotAssignedException {

  private NamespaceIdentifier getNamespaceIdentifierInstance() {
    ArrayList<String> name = new ArrayList<>(1);
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
  
  public VariableNotAssignedException getInstance(){
    return new VariableNotAssignedException(getVariableInstance());
  }
  
  @Test
  public void testGetMessage_containsVariableIdentifier() {
    VariableNotAssignedException instance = getInstance();
    String message = instance.getMessage();
    assertTrue(message.contains(
        getVariableInstance().getIdentifier().toString())
    );
  }

}
