package org.manifold.frontend.syntaxtree;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import org.manifold.frontend.syntaxtree.NamespaceIdentifier;
import org.manifold.frontend.syntaxtree.VariableIdentifier;
import org.manifold.frontend.syntaxtree.VariableNotDefinedException;

public class TestVariableNotDefinedException {

  private NamespaceIdentifier getNamespaceIdentifierInstance() {
    ArrayList<String> name = new ArrayList<String>(1);
    name.add("whdl");
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
