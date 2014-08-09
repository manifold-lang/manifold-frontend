package org.manifold.compiler.front;

import static org.junit.Assert.*;

import org.junit.Test;
import org.manifold.compiler.BooleanTypeValue;
import org.manifold.compiler.BooleanValue;
import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

public class TestVariable {
  private NamespaceIdentifier getNamespaceIdentifier() {
    return new NamespaceIdentifier("whdl:is:cool");
  }

  private VariableIdentifier getVariableIdentifier() {
    return new VariableIdentifier(getNamespaceIdentifier(), "foo");
  }
  
  private Scope getScope() {
    return new Scope();
  }
  
  private Variable getVariable() {
    // declare "foo" as a variable that stores a Type
    return new Variable(
        getScope(),
        getVariableIdentifier(),
        getTypeExpression()
    );
  }
  
  private TypeValue getTypeValue() {
    return BooleanTypeValue.getInstance();
  }
  
  private Value getValue() {
    return BooleanValue.getInstance(true);
  }

  private Expression getTypeExpression(){
    return new LiteralExpression(getTypeValue());
  }

  private Expression getValueExpression(){
    return new LiteralExpression(getValue());
  }

  @Test
  public void testGetIdentifier() {
    assertEquals(getVariable().getIdentifier(), getVariableIdentifier());
  }

  @Test
  public void testGetTypeValue() throws TypeMismatchException {
    assertEquals(
        getVariable().getType(),
        getTypeExpression().evaluate(getVariable().getScope())
    );
  }

  public void testGetValueUnassigned() throws VariableNotAssignedException {
    Variable v = getVariable();
    assertFalse(v.isAssigned());
    assertNull(v.getValue());
  }

  @Test
  public void testSetValue() throws
      MultipleAssignmentException,
      VariableNotAssignedException {
    Variable v = getVariable();
    v.setValueExpression(getValueExpression());
    assertEquals(v.getValue(), getValue());
  }

  @Test(expected = MultipleAssignmentException.class)
  public void testSetValueMultiple() throws MultipleAssignmentException {
    Variable v = getVariable();
    v.setValueExpression(getValueExpression());
    v.setValueExpression(getValueExpression());
  }

  @Test(expected = TypeMismatchException.class)
  public void verify_nontypeThrow() throws
      TypeMismatchException,
      MultipleAssignmentException {

    Variable v = new Variable(
        getScope(),
        getVariableIdentifier(),
        new LiteralExpression(BooleanValue.getInstance(false))
    );
    v.verify();
  }

  @Test(expected = TypeMismatchException.class)
  public void verify_typeMismatchThrow() throws
      TypeMismatchException,
      MultipleAssignmentException {

    Variable v = new Variable(
        getScope(),
        getVariableIdentifier(),
        new LiteralExpression(BooleanTypeValue.getInstance())
    );
    v.setValueExpression(new LiteralExpression(BooleanTypeValue.getInstance()));
    v.verify();
  }
}
