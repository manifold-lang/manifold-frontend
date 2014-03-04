package org.whdl.frontend.syntaxtree;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class TestVariableNotAssignedException {

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
  
  public VariableNotAssignedException getInstance(){
    return new VariableNotAssignedException(getVariableInstance());
  }
  
  @Test
  public void testGetMessage_containsVariableIdentifier() {
    VariableNotAssignedException instance = getInstance();
    String message = instance.getMessage();
    assertTrue(message.contains(getVariableInstance().getIdentifier().toString()));
  }

}
