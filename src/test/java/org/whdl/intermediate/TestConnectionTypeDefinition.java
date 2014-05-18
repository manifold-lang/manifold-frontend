package org.whdl.intermediate;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestConnectionTypeDefinition {

  @Test
  public void testInstantiate_no_attributes() {
    ConnectionTypeDefinition conTypeDef = new ConnectionTypeDefinition("foo");
    Value con = conTypeDef.instantiate();
    // the Value we receive must be a Connection
    assertTrue("instanted value is not a Connection", con instanceof Connection);
  }

  @Test
  public void testInstantiate_with_attributes() throws UndeclaredIdentifierException {
    ConnectionTypeDefinition conTypeDef = new ConnectionTypeDefinition("foo");
    // add one simple attribute
    TypeTypeDefinition attrTTD = new TypeTypeDefinition("attr-type", new PrimitiveType(PrimitiveType.PrimitiveKind.BOOLEAN));
    conTypeDef.addAttribute("attr", attrTTD);
    
    Connection con = (Connection)conTypeDef.instantiate();
    Value attr = con.getAttribute("attr"); // it is sufficient that we return /something/ without throwing an exception
  }
  
}
