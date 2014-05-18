package org.whdl.intermediate;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestEndpoint {

  private static final EndpointType defaultEndpointDefinition = new EndpointType("foo");

  @Test
  public void testGetAttribute() throws UndeclaredIdentifierException {
    Endpoint ept = new Endpoint(defaultEndpointDefinition);
    Value v = new BooleanValue(true);
    ept.setAttribute("v", v);
    Value vActual = ept.getAttribute("v");
    assertEquals(v, vActual);
  }

  @Test(expected=org.whdl.intermediate.UndeclaredIdentifierException.class)
  public void testGetAttribute_nonexistent() throws UndeclaredIdentifierException {
    Endpoint ept = new Endpoint(defaultEndpointDefinition);
    Value vBogus = ept.getAttribute("bogus");
  }
  
  @Test
  public void testSetAttribute() {
    Endpoint ept = new Endpoint(defaultEndpointDefinition);
    Value v = new BooleanValue(true);
    ept.setAttribute("v", v);
  }
  
  @Test
  public void testSetAttribute_multiple_set() {
    // setting an attribute twice should just work
    Endpoint ept = new Endpoint(defaultEndpointDefinition);
    Value v = new BooleanValue(true);
    ept.setAttribute("v", v);
    Value v2 = new BooleanValue(false);
    ept.setAttribute("v", v2);
  }

}
