package org.whdl.intermediate;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestConstraintTypeDefinition {

  @Test
  public void testInstantiate_no_arguments() {
    ConstraintTypeDefinition conTypeDef = new ConstraintTypeDefinition("foo");
    Value con = conTypeDef.instantiate("con");
    // the Value we receive must be a Constraint
    assertTrue("instanted value is not a Constraint", con instanceof Constraint);
  }

  @Test
  public void testInstantiate_with_arguments() throws UndeclaredIdentifierException {
    ConstraintTypeDefinition conTypeDef = new ConstraintTypeDefinition("foo");
    // add one simple attribute
    TypeTypeDefinition attrTTD = new TypeTypeDefinition("attr-type", new PrimitiveType(PrimitiveType.PrimitiveKind.BOOLEAN));
    conTypeDef.addArgument("attr", attrTTD);
    
    Constraint con = (Constraint)conTypeDef.instantiate("ept");
    Value attr = con.getArgument("attr"); // it is sufficient that we return /something/ without throwing an exception
  }
  
}
