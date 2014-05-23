package org.whdl.intermediate;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestConnectionType {

  @Test
  public void testInstantiate_no_attributes() throws TypeMismatchException {
    ConnectionType conTypeDef = new ConnectionType();
    Value con = conTypeDef.instantiate();
    // the Value we receive must be a Connection
    assertTrue("instanted value is not a Connection", con instanceof Connection);
  }

  @Test
  public void testInstantiate_with_attributes() throws UndeclaredIdentifierException, TypeMismatchException {
    ConnectionType conTypeDef = new ConnectionType();
    // add one simple attribute
    UserDefinedType attrTTD = new UserDefinedType(new PrimitiveType(PrimitiveType.PrimitiveKind.BOOLEAN));
    conTypeDef.addAttribute("attr", attrTTD);
    
    Connection con = (Connection)conTypeDef.instantiate();
    Value attr = con.getAttribute("attr"); // it is sufficient that we return /something/ without throwing an exception
  }
  
}
