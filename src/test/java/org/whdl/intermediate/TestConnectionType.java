package org.whdl.intermediate;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestConnectionType {

  @Test
  public void testInstantiate_no_attributes() {
    ConnectionType conTypeDef = new ConnectionType("foo");
    Value con = conTypeDef.instantiate();
    // the Value we receive must be a Connection
    assertTrue("instanted value is not a Connection", con instanceof Connection);
  }

  @Test
  public void testInstantiate_with_attributes() throws UndeclaredIdentifierException {
    ConnectionType conTypeDef = new ConnectionType("foo");
    // add one simple attribute
    UserDefinedType attrTTD = new UserDefinedType("attr-type", new PrimitiveType(PrimitiveType.PrimitiveKind.BOOLEAN));
    conTypeDef.addAttribute("attr", attrTTD);
    
    Connection con = (Connection)conTypeDef.instantiate();
    Value attr = con.getAttribute("attr"); // it is sufficient that we return /something/ without throwing an exception
  }
  
}
