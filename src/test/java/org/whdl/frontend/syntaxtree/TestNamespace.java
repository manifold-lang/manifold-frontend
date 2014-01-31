package org.whdl.frontend.syntaxtree;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Test;

public class TestNamespace {

  private NamespaceIdentifier getNamespaceIdentifier() {
    return new NamespaceIdentifier("whdl:is:cool");
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

}
