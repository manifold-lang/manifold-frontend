package org.whdl.intermediate;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestEndpoint {

  private static final EndpointTypeDefinition defaultEndpointDefinition = new EndpointTypeDefinition("foo");
  
  @Test
  public void testGetDefinition() {
    Endpoint ept = new Endpoint("foo-1", defaultEndpointDefinition);
    assertEquals(defaultEndpointDefinition, ept.getDefinition());
  }

  @Test
  public void testGetAttribute() throws UndeclaredIdentifierException {
    Endpoint ept = new Endpoint("foo-1", defaultEndpointDefinition);
    Value v = new BooleanValue("v", true);
    ept.setAttribute("v", v);
    Value vActual = ept.getAttribute("v");
    assertEquals(v, vActual);
  }

  @Test(expected=org.whdl.intermediate.UndeclaredIdentifierException.class)
  public void testGetAttribute_nonexistent() throws UndeclaredIdentifierException {
    Endpoint ept = new Endpoint("foo-1", defaultEndpointDefinition);
    Value vBogus = ept.getAttribute("bogus");
  }
  
  @Test
  public void testSetAttribute() {
    Endpoint ept = new Endpoint("foo-1", defaultEndpointDefinition);
    Value v = new BooleanValue("v", true);
    ept.setAttribute("v", v);
  }
  
  @Test
  public void testSetAttribute_multiple_set() {
    // setting an attribute twice should just work
    Endpoint ept = new Endpoint("foo-1", defaultEndpointDefinition);
    Value v = new BooleanValue("v", true);
    ept.setAttribute("v", v);
    Value v2 = new BooleanValue("v", false);
    ept.setAttribute("v", v2);
  }

}
