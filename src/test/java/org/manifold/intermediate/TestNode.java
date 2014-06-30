package org.manifold.intermediate;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.manifold.intermediate.BooleanType;
import org.manifold.intermediate.BooleanValue;
import org.manifold.intermediate.Node;
import org.manifold.intermediate.NodeType;
import org.manifold.intermediate.Port;
import org.manifold.intermediate.PortType;
import org.manifold.intermediate.Type;
import org.manifold.intermediate.UndeclaredAttributeException;
import org.manifold.intermediate.UndeclaredIdentifierException;
import org.manifold.intermediate.Value;

public class TestNode {

  private static final PortType defaultPortDefinition = new PortType(new HashMap<String, Type>());
  private static final Type boolType = BooleanType.getInstance();
  private static final String PORT_NAME = "testport";
  private static final String PORT_ATTR_KEY = "the truth will set you free";
  
  private Node n;
  
  @Before
  public void setup() {
    HashMap<String, PortType> portMap = new HashMap<>();
    portMap.put(PORT_NAME, defaultPortDefinition);
    n = new Node(new NodeType(new HashMap<String, Type>(), portMap));
  }

  @Test
  public void testGetAttribute() throws UndeclaredAttributeException {
    Value v = new BooleanValue(boolType, true);
    n.setAttribute("abc", v);
    Value vActual = n.getAttribute("abc");
    assertEquals(v, vActual);
  }

  @Test(expected = org.manifold.intermediate.UndeclaredAttributeException.class)
  public void testGetAttribute_nonexistent() throws UndeclaredAttributeException {
    Value vBogus = n.getAttribute("bogus");
  }

  @Test
  public void testSetAttribute() {
    Value v = new BooleanValue(boolType, true);
    n.setAttribute("abc", v);
  }

  @Test
  public void testSetAttribute_multiple_set() {
    // setting an attribute and then setting it again should just work
    Value v = new BooleanValue(boolType, true);
    n.setAttribute("abc", v);
    v = new BooleanValue(boolType, false);
    n.setAttribute("abc", v);
  }

  @Test
  public void testGetPort() throws UndeclaredIdentifierException {
    Port port = n.getPort(PORT_NAME);
    assertEquals(defaultPortDefinition, port.getType());
  }

  @Test(expected = org.manifold.intermediate.UndeclaredIdentifierException.class)
  public void testGetPort_nonexistent() throws UndeclaredIdentifierException {
    Port ptBogus = n.getPort("bogus");
  }

  @Test
  public void testSetPortAttribute() throws UndeclaredIdentifierException, UndeclaredAttributeException {
    Value v = new BooleanValue(boolType, true);
    n.getPort(PORT_NAME);
    n.setPortAttributes(PORT_NAME, PORT_ATTR_KEY, v);
    assertEquals(v, n.getPort(PORT_NAME).getAttribute(PORT_ATTR_KEY));
  }
}
