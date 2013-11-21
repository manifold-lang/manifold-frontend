package org.whdl.frontend.syntaxtree;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class TestVariable {
    private NamespaceIdentifier getNamespaceIdentifier() {
      ArrayList<String> name = new ArrayList<String>(3);
      name.add("whdl");
      name.add("is");
      name.add("cool");
  
      return new NamespaceIdentifier(name);
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
