package org.whdl.frontend.syntaxtree;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class TestNamespace {

  // because we just need something that returns a type, without bringing too
  // much else into the works
  // FIXME(lucas) Remove this once we have an actual LiteralExpression
  static private class FacadeExpression extends Expression {

    private Value value;

    public FacadeExpression(Value value) {
      this.value = value;
    }

    @Override
    public Value evaluate() {
      return value;
    }

    @Override
    public TypeValue getType() {
      return value.getType();
    }

  }

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
    return new FacadeExpression(BitTypeValue.getInstance());
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
  public void getVariable_publicScope_success()
      throws MultipleDefinitionException, VariableNotDefinedException {
    // depends on correctness of Scope
    Namespace n = new Namespace(getNamespaceIdentifier());
    n.getPublicScope().defineVariable(getVariableIdentifier(),
        getTypeExpression());
    Variable v = n.getVariable(getVariableIdentifier());
  }

  @Test(expected = VariableNotDefinedException.class)
  public void getVariable_privateScope_throwsVariableNotDefined()
      throws MultipleDefinitionException, VariableNotDefinedException {
    // depends on correctness of Scope
    Namespace n = new Namespace(getNamespaceIdentifier());
    n.getPrivateScope().defineVariable(getVariableIdentifier(),
        getTypeExpression());
    Variable v = n.getVariable(getVariableIdentifier());
  }

}
