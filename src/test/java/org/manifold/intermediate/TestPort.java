package org.manifold.intermediate;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

public class TestPort {

  private static final PortType defaultPortType = new PortType(new HashMap<String, Type>());
  private static final Type boolType = BooleanType.getInstance();
  private static final String PORT_NAME = "testport";
  
  private Node parent;
  private Port port;
  
  @Before
  public void setup() {
    HashMap<String, PortType> portMap = new HashMap<>();
    portMap.put(PORT_NAME, defaultPortType);
    parent = new Node(new NodeType(new HashMap<String, Type>(), portMap));
    
    port = new Port(defaultPortType, parent);
  }

  @Test
  public void testGetAttribute() throws UndeclaredAttributeException {
    Value v = new BooleanValue(boolType, true);
    port.setAttribute("v", v);
    Value vActual = port.getAttribute("v");
    assertEquals(v, vActual);
  }

  @Test(expected = org.manifold.intermediate.UndeclaredAttributeException.class)
  public void testGetAttribute_nonexistent() throws UndeclaredAttributeException {
    Value vBogus = port.getAttribute("bogus");
  }

  @Test
  public void testSetAttribute() {
    Value v = new BooleanValue(boolType, true);
    port.setAttribute("v", v);
  }

  @Test
  public void testSetAttribute_multiple_set() {
    // setting an attribute twice should just work
    Value v = new BooleanValue(boolType, true);
    port.setAttribute("v", v);
    Value v2 = new BooleanValue(boolType, false);
    port.setAttribute("v", v2);
  }
  
  @Test
  public void testGetParent() throws UndeclaredIdentifierException {
    assertEquals(parent, parent.getPort(PORT_NAME).getParent());
  }
}
