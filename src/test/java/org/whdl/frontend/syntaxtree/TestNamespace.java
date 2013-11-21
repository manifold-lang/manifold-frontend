package org.whdl.frontend.syntaxtree;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class TestNamespace {

  private NamespaceIdentifier getNamespaceIdentifier() {
    ArrayList<String> name = new ArrayList<String>(3);
    name.add("whdl");
    name.add("is");
    name.add("cool");

    return new NamespaceIdentifier(name);
  }

  private VariableIdentifier getVariableIdentifier() {
    VariableIdentifier id = new VariableIdentifier(getNamespaceIdentifier(),
        "foo");

    return id;
  }

  private Expression getTypeExpression() {
    return new LiteralExpression(BitTypeValue.getInstance());
  }

  @Test
  public void testGetAbsoluteName() {
    Namespace n = new Namespace(getNamespaceIdentifier());
    assertEquals(getNamespaceIdentifier(), n.getAbsoluteName());
  }

  @Test
  public void privateScope_distinctFrom_publicScope() {
    Namespace n = new Namespace(getNamespaceIdentifier());
    Scope priv = n.getPrivateScope();
    Scope pub = n.getPublicScope();
    assertNotSame("private scope and public scope not distinct", priv, pub);
  }

  @Test
  public void containsIdentifier_publicScope_success() {
    fail("not implemented yet");
  }

  @Test
  public void containsIdentifier_privateScope_fail() {
    fail("not implemented yet");
  }

  @Test
  public void getVariable_publicScope_success()
      throws MultipleDefinitionException, VariableNotDefinedException {
    // depends on correctness of Scope
    Namespace n = new Namespace(getNamespaceIdentifier());
    n.getPublicScope().defineVariable(getVariableIdentifier(),
        getTypeExpression());
    Variable v = n.getVariable(getVariableIdentifier());
    assertEquals(getVariableIdentifier(), v.getIdentifier());
  }

  @Test(expected = VariableNotDefinedException.class)
  public void getVariable_privateScope_throwsVariableNotDefined()
      throws MultipleDefinitionException, VariableNotDefinedException {
    // depends on correctness of Scope
    Namespace n = new Namespace(getNamespaceIdentifier());
    n.getPrivateScope().defineVariable(getVariableIdentifier(),
        getTypeExpression());
    Variable v = n.getVariable(getVariableIdentifier());
    fail("somehow retrieved a variable defined in a private scope");
  }

}
