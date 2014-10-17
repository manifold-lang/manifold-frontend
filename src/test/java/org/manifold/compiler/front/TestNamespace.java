package org.manifold.compiler.front;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;
import org.manifold.compiler.BooleanTypeValue;

public class TestNamespace {

  private NamespaceIdentifier getNamespaceIdentifier() {
    return new NamespaceIdentifier("manifold:is:cool");
  }

  private VariableIdentifier getVariableIdentifier() {
    VariableIdentifier id = new VariableIdentifier(
        getNamespaceIdentifier(),
        "foo"
    );

    return id;
  }

  private Expression getTypeExpression() {
    return new LiteralExpression(BooleanTypeValue.getInstance());
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

}
