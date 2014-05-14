package org.whdl.intermediate;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestNode {

  private static final NodeTypeDefinition defaultNodeDefinition = new NodeTypeDefinition("foo");
  private static final EndpointTypeDefinition defaultEndpointDefinition = new EndpointTypeDefinition("ept");
  
  @Test
  public void testGetDefinition() {
    Node n = new Node("foo-1", defaultNodeDefinition);
    assertEquals(defaultNodeDefinition, n.getDefinition());
  }

  @Test
  public void testGetAttribute() throws UndeclaredIdentifierException {
    Node n = new Node("foo-1", defaultNodeDefinition);
    Value v = new BooleanValue("foo-1.abc", true);
    n.setAttribute("abc", v);
    Value vActual = n.getAttribute("abc");
    assertEquals(v, vActual);
  }
  
  @Test(expected=org.whdl.intermediate.UndeclaredIdentifierException.class)
  public void testGetAttribute_nonexistent() throws UndeclaredIdentifierException {
    Node n = new Node("foo-1", defaultNodeDefinition);
    Value vBogus = n.getAttribute("bogus");
  }

  @Test
  public void testSetAttribute() {
    Node n = new Node("foo-1", defaultNodeDefinition);
    Value v = new BooleanValue("foo-1.abc", true);
    n.setAttribute("abc", v);
  }
  
  @Test
  public void testSetAttribute_multiple_set() {
    // setting an attribute and then setting it again should just work
    Node n = new Node("foo-1", defaultNodeDefinition);
    Value v = new BooleanValue("foo-1.abc", true);
    n.setAttribute("abc", v);
    v = new BooleanValue("foo-1.abc", false);
    n.setAttribute("abc", v);
  }

  @Test
  public void testGetEndpoint() throws UndeclaredIdentifierException {
    Node n = new Node("foo-1", defaultNodeDefinition);
    Endpoint ept1 = new Endpoint("ept-1", defaultEndpointDefinition);
    n.setEndpoint("ept1", ept1);
    Endpoint eptActual = n.getEndpoint("ept1");
    assertEquals(ept1, eptActual);
  }
  
  @Test(expected=org.whdl.intermediate.UndeclaredIdentifierException.class)
  public void testGetEndpoint_nonexistent() throws UndeclaredIdentifierException {
    Node n = new Node("foo-1", defaultNodeDefinition);
    Endpoint eptBogus = n.getEndpoint("bogus");  }

  @Test
  public void testSetEndpoint() {
    Node n = new Node("foo-1", defaultNodeDefinition);
    Endpoint ept1 = new Endpoint("ept-1", defaultEndpointDefinition);
    n.setEndpoint("ept1", ept1);
  }
  
  @Test
  public void testSetEndpoint_multiple_set() {
    // setting an endpoint and then setting it again should just work
    Node n = new Node("foo-1", defaultNodeDefinition);
    Endpoint ept1 = new Endpoint("ept-1", defaultEndpointDefinition);
    n.setEndpoint("ept1", ept1);
    Endpoint ept2 = new Endpoint("ept-2", defaultEndpointDefinition);
    n.setEndpoint("ept1", ept2);
  }

}
