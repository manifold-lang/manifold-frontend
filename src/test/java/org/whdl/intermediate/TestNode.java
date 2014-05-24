package org.whdl.intermediate;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

public class TestNode {

  private static final NodeType defaultNodeDefinition = new NodeType(new HashMap<String, Type>(), new HashMap<String, EndpointType>());
  private static final EndpointType defaultEndpointDefinition = new EndpointType(new HashMap<String, Type>());

  @Test
  public void testGetAttribute() throws UndeclaredIdentifierException {
    Node n = new Node(defaultNodeDefinition);
    Value v = new BooleanValue(new BooleanType(), true);
    n.setAttribute("abc", v);
    Value vActual = n.getAttribute("abc");
    assertEquals(v, vActual);
  }
  
  @Test(expected=org.whdl.intermediate.UndeclaredIdentifierException.class)
  public void testGetAttribute_nonexistent() throws UndeclaredIdentifierException {
    Node n = new Node(defaultNodeDefinition);
    Value vBogus = n.getAttribute("bogus");
  }

  @Test
  public void testSetAttribute() {
    Node n = new Node(defaultNodeDefinition);
    Value v = new BooleanValue(new BooleanType(), true);
    n.setAttribute("abc", v);
  }
  
  @Test
  public void testSetAttribute_multiple_set() {
    // setting an attribute and then setting it again should just work
    Type boolType = new BooleanType();
    Node n = new Node(defaultNodeDefinition);
    Value v = new BooleanValue(boolType, true);
    n.setAttribute("abc", v);
    v = new BooleanValue(boolType, false);
    n.setAttribute("abc", v);
  }

  @Test
  public void testGetEndpoint() throws UndeclaredIdentifierException {
    Node n = new Node(defaultNodeDefinition);
    Endpoint ept1 = new Endpoint(defaultEndpointDefinition);
    n.setEndpoint("ept1", ept1);
    Endpoint eptActual = n.getEndpoint("ept1");
    assertEquals(ept1, eptActual);
  }
  
  @Test(expected=org.whdl.intermediate.UndeclaredIdentifierException.class)
  public void testGetEndpoint_nonexistent() throws UndeclaredIdentifierException {
    Node n = new Node(defaultNodeDefinition);
    Endpoint eptBogus = n.getEndpoint("bogus");  }

  @Test
  public void testSetEndpoint() {
    Node n = new Node(defaultNodeDefinition);
    Endpoint ept1 = new Endpoint(defaultEndpointDefinition);
    n.setEndpoint("ept1", ept1);
  }
  
  @Test
  public void testSetEndpoint_multiple_set() {
    // setting an endpoint and then setting it again should just work
    Node n = new Node(defaultNodeDefinition);
    Endpoint ept1 = new Endpoint(defaultEndpointDefinition);
    n.setEndpoint("ept1", ept1);
    Endpoint ept2 = new Endpoint(defaultEndpointDefinition);
    n.setEndpoint("ept1", ept2);
  }

}
