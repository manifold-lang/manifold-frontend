package org.whdl.intermediate;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestEndpointType {

  @Test
  public void testInstantiate_no_attributes() {
    EndpointType eptTypeDef = new EndpointType();
    Value ept = eptTypeDef.instantiate();
    // the Value we receive must be an Endpoint
    assertTrue("instanted value is not an Endpoint", ept instanceof Endpoint);
  }

  @Test
  public void testInstantiate_with_attributes() throws UndeclaredIdentifierException {
    EndpointType eptTypeDef = new EndpointType();
    // add one simple attribute
    UserDefinedType attrTTD = new UserDefinedType(new PrimitiveType(PrimitiveType.PrimitiveKind.BOOLEAN));
    eptTypeDef.addAttribute("attr", attrTTD);
    
    Endpoint ept = (Endpoint)eptTypeDef.instantiate();
    Value attr = ept.getAttribute("attr"); // it is sufficient that we return /something/ without throwing an exception
  }
  
}
