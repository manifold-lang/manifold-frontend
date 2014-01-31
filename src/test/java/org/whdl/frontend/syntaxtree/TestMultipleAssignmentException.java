package org.whdl.frontend.syntaxtree;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class TestMultipleAssignmentException {

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

  private Expression getTypeExpression(){
    return new LiteralExpression(BitTypeValue.getInstance());
  }
  
  private Variable v_inst = null; 
  public Variable getVariableInstance(){
    if(v_inst == null){
      v_inst = new Variable(getVariableIdentifierInstance(), getTypeExpression());
    }
    return v_inst;
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
