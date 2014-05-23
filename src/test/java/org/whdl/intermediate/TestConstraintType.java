package org.whdl.intermediate;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestConstraintType {

  @Test
  public void testInstantiate_no_arguments() {
    ConstraintType conTypeDef = new ConstraintType();
    Value con = conTypeDef.instantiate();
    // the Value we receive must be a Constraint
    assertTrue("instanted value is not a Constraint", con instanceof Constraint);
  }

  @Test
  public void testInstantiate_with_arguments() throws UndeclaredIdentifierException {
    ConstraintType conTypeDef = new ConstraintType();
    // add one simple attribute
    UserDefinedType attrTTD = new UserDefinedType(new PrimitiveType(PrimitiveType.PrimitiveKind.BOOLEAN));
    conTypeDef.addArgument("attr", attrTTD);
    
    Constraint con = (Constraint)conTypeDef.instantiate();
    Value attr = con.getArgument("attr"); // it is sufficient that we return /something/ without throwing an exception
  }
  
}
