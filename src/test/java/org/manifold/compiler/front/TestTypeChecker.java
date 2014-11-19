package org.manifold.compiler.front;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.manifold.compiler.BooleanTypeValue;
import org.manifold.compiler.BooleanValue;
import org.manifold.compiler.TypeValue;

public class TestTypeChecker {

  private Map<NamespaceIdentifier, Namespace> namespaces;
  private NamespaceIdentifier defaultNamespaceID;
  private Namespace defaultNamespace;

  @Before
  public void setupEnvironment() {
    namespaces = new HashMap<>();
    defaultNamespaceID = new NamespaceIdentifier("");
    defaultNamespace = new Namespace(defaultNamespaceID);
    namespaces.put(defaultNamespaceID, defaultNamespace);
  }

  // helper function to quickly create a binding in the default namespace
  private void bind(String name, Expression value)
      throws MultipleDefinitionException, VariableNotDefinedException,
      MultipleAssignmentException {
    VariableIdentifier id = new VariableIdentifier(defaultNamespaceID, name);
    defaultNamespace.getPrivateScope().defineVariable(id);
    defaultNamespace.getPrivateScope().assignVariable(id, value);
  }

  // helper function to quickly get the type of a variable in the default n.s.
  private TypeValue getType(String name)
      throws TypeMismatchException, VariableNotDefinedException {
    VariableIdentifier id = new VariableIdentifier(defaultNamespaceID, name);
    return defaultNamespace.getPrivateScope().getVariableType(id);
  }

  @Test
  public void testTypeAssignment_Literal()
      throws MultipleDefinitionException, VariableNotDefinedException,
      MultipleAssignmentException, VariableNotAssignedException,
      TypeMismatchException {
    // "a = false;"
    // expect a ::= Bool
    bind("a", new LiteralExpression(BooleanValue.getInstance(false)));
    TypeChecker.typecheck(namespaces, defaultNamespace);
    assertEquals(BooleanTypeValue.getInstance(), getType("a"));
  }

}
