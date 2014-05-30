package org.whdl.intermediate;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

public class TestNode {

  private static final NodeType defaultNodeDefinition = new NodeType(new HashMap<String, Type>(), new HashMap<String, PortType>());
  private static final PortType defaultPortDefinition = new PortType(new HashMap<String, Type>());
  private static final Type boolType = BooleanType.getInstance();

  @Test
  public void testGetAttribute() throws UndeclaredIdentifierException {
    Node n = new Node(defaultNodeDefinition);
    Value v = new BooleanValue(boolType, true);
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
    Value v = new BooleanValue(boolType, true);
    n.setAttribute("abc", v);
  }
  
  @Test
  public void testSetAttribute_multiple_set() {
    // setting an attribute and then setting it again should just work
    Node n = new Node(defaultNodeDefinition);
    Value v = new BooleanValue(boolType, true);
    n.setAttribute("abc", v);
    v = new BooleanValue(boolType, false);
    n.setAttribute("abc", v);
  }

  @Test
  public void testGetPort() throws UndeclaredIdentifierException {
    Node n = new Node(defaultNodeDefinition);
    Port ept1 = new Port(defaultPortDefinition);
    n.setPort("ept1", ept1);
    Port eptActual = n.getPort("ept1");
    assertEquals(ept1, eptActual);
  }
  
  @Test(expected=org.whdl.intermediate.UndeclaredIdentifierException.class)
  public void testGetPort_nonexistent() throws UndeclaredIdentifierException {
    Node n = new Node(defaultNodeDefinition);
    Port eptBogus = n.getPort("bogus");  }

  @Test
  public void testSetPort() {
    Node n = new Node(defaultNodeDefinition);
    Port ept1 = new Port(defaultPortDefinition);
    n.setPort("ept1", ept1);
  }
  
  @Test
  public void testSetPort_multiple_set() {
    // setting a port and then setting it again should just work
    Node n = new Node(defaultNodeDefinition);
    Port ept1 = new Port(defaultPortDefinition);
    n.setPort("ept1", ept1);
    Port ept2 = new Port(defaultPortDefinition);
    n.setPort("ept1", ept2);
  }

}
