package org.manifold.frontend.syntaxtree;

import static org.junit.Assert.*;

import org.junit.Test;
import org.manifold.frontend.syntaxtree.BitTypeValue;
import org.manifold.frontend.syntaxtree.BitValue;
import org.manifold.frontend.syntaxtree.Expression;
import org.manifold.frontend.syntaxtree.LiteralExpression;
import org.manifold.frontend.syntaxtree.MultipleAssignmentException;
import org.manifold.frontend.syntaxtree.NamespaceIdentifier;
import org.manifold.frontend.syntaxtree.TypeMismatchException;
import org.manifold.frontend.syntaxtree.Variable;
import org.manifold.frontend.syntaxtree.VariableIdentifier;
import org.manifold.frontend.syntaxtree.VariableNotAssignedException;

public class TestVariable {
    private NamespaceIdentifier getNamespaceIdentifier() {
      return new NamespaceIdentifier("whdl:is:cool");
    }
  
    private VariableIdentifier getVariableIdentifier() {
      return new VariableIdentifier(getNamespaceIdentifier(), "foo");
    }
  
    private Variable getVariable() {
      // declare "foo" as a variable that stores a Type
      return new Variable(getVariableIdentifier(), getTypeExpression());
    }
    
    private Expression getTypeExpression(){
      return new LiteralExpression(BitTypeValue.getInstance());
    }
    
    private Expression getValueExpression(){
      return new LiteralExpression(BitValue.getInstance(true));
    }
  
    @Test
    public void testGetIdentifier() {
      assertEquals(getVariable().getIdentifier(), getVariableIdentifier());
    }
  
    @Test
    public void testGetTypeValue() throws TypeMismatchException {
      assertEquals(getVariable().getType(), getTypeExpression().evaluate());
    }
    
    @Test(expected=VariableNotAssignedException.class)
    public void testGetValueUnassigned() throws VariableNotAssignedException {
      Variable v = getVariable();
      assertFalse(v.isAssigned());
      v.getValue();
    }
    
    @Test
    public void testSetValue() throws
        MultipleAssignmentException,
        VariableNotAssignedException {
      Variable v = getVariable();
      v.setValue(getValueExpression());
      assertEquals(getValueExpression().evaluate(), v.getValue());
    }
    
    @Test(expected=MultipleAssignmentException.class)
    public void testSetValueMultiple() throws MultipleAssignmentException {
      Variable v = getVariable();
      v.setValue(getValueExpression());
      v.setValue(getValueExpression());
    }
    
    @Test(expected=TypeMismatchException.class)
    public void verify_nontypeThrow() throws
        TypeMismatchException,
        MultipleAssignmentException {
 
      Variable v = new Variable(
          getVariableIdentifier(),
          new LiteralExpression(BitValue.getInstance(false))
      );
      v.verify();
    }
    
    @Test(expected=TypeMismatchException.class)
    public void verify_typeMismatchThrow() throws
        TypeMismatchException,
        MultipleAssignmentException {
 
      Variable v = new Variable(
          getVariableIdentifier(),
          new LiteralExpression(BitTypeValue.getInstance())
      );
      v.setValue(new LiteralExpression(BitTypeValue.getInstance()));
      v.verify();
    }
}
