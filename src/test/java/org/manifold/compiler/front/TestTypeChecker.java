package org.manifold.compiler.front;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.LogManager;
import org.apache.log4j.PatternLayout;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.manifold.compiler.BooleanTypeValue;
import org.manifold.compiler.BooleanValue;
import org.manifold.compiler.TypeValue;

public class TestTypeChecker {

  @BeforeClass
  public static void setupLogging() {
    PatternLayout layout = new PatternLayout(
        "%-5p [%t]: %m%n");
    LogManager.getRootLogger().removeAllAppenders();
    LogManager.getRootLogger().addAppender(
        new ConsoleAppender(layout, ConsoleAppender.SYSTEM_ERR));
  }

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

  // helper function to quickly reference a variable in the default namespace
  private Expression var(String name) {
    VariableIdentifier id = new VariableIdentifier(defaultNamespaceID, name);
    return new VariableReferenceExpression(id);
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

  @Test
  public void testTypeAssignment_InferredLiteral()
      throws MultipleDefinitionException, VariableNotDefinedException,
      MultipleAssignmentException, VariableNotAssignedException,
      TypeMismatchException {
    // "a = false;"
    // "b = a;"
    // expect b ::= Bool
    bind("a", new LiteralExpression(BooleanValue.getInstance(false)));
    bind("b", var("a"));
    TypeChecker.typecheck(namespaces, defaultNamespace);
    assertEquals(BooleanTypeValue.getInstance(), getType("b"));
  }

  @Test
  public void testTypeAssignment_PrimitiveFunction()
      throws MultipleDefinitionException, VariableNotDefinedException,
      MultipleAssignmentException, VariableNotAssignedException,
      TypeMismatchException {
    // not ::= Bool -> Bool
    FunctionTypeValue notPrimitiveType = new FunctionTypeValue(
        BooleanTypeValue.getInstance(), BooleanTypeValue.getInstance());
    PrimitiveFunctionValue notPrimitive = new PrimitiveFunctionValue(
        "not", notPrimitiveType, null);
    bind("not", new LiteralExpression(notPrimitive));
    // make a fresh typevalue to prevent simple object-identity checking
    TypeValue expectedType = new FunctionTypeValue(
        BooleanTypeValue.getInstance(), BooleanTypeValue.getInstance());

    TypeChecker.typecheck(namespaces, defaultNamespace);
    assertEquals(expectedType, getType("not"));
  }

}
