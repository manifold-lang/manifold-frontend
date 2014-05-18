package org.whdl.intermediate;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestConstraint {

  private static final ConstraintType defaultConstraintDefinition = new ConstraintType("foo");

  @Test
  public void testGetArgument() throws UndeclaredIdentifierException {
    Constraint ept = new Constraint(defaultConstraintDefinition);
    Value v = new BooleanValue(true);
    ept.setArgument("v", v);
    Value vActual = ept.getArgument("v");
    assertEquals(v, vActual);
  }

  @Test(expected=org.whdl.intermediate.UndeclaredIdentifierException.class)
  public void testGetArgument_nonexistent() throws UndeclaredIdentifierException {
    Constraint ept = new Constraint(defaultConstraintDefinition);
    Value vBogus = ept.getArgument("bogus");
  }
  
  @Test
  public void testSetArgument() {
    Constraint ept = new Constraint(defaultConstraintDefinition);
    Value v = new BooleanValue(true);
    ept.setArgument("v", v);
  }
  
  @Test
  public void testSetArgument_multiple_set() {
    // setting an Argument twice should just work
    Constraint ept = new Constraint(defaultConstraintDefinition);
    Value v = new BooleanValue(true);
    ept.setArgument("v", v);
    Value v2 = new BooleanValue(false);
    ept.setArgument("v", v2);
  }

}
