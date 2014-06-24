package org.manifold.intermediate;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

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

  private static final NodeType defaultNodeDefinition = new NodeType(new HashMap<String, Type>(), new HashMap<String, PortType>());
  private static final PortType defaultPortDefinition = new PortType(new HashMap<String, Type>());
  private static final Type boolType = BooleanType.getInstance();

  @Test
  public void testGetAttribute() throws UndeclaredAttributeException {
    Node n = new Node(defaultNodeDefinition);
    Value v = new BooleanValue(boolType, true);
    n.setAttribute("abc", v);
    Value vActual = n.getAttribute("abc");
    assertEquals(v, vActual);
  }

  @Test(expected = org.manifold.intermediate.UndeclaredAttributeException.class)
  public void testGetAttribute_nonexistent() throws UndeclaredAttributeException {
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
    Port pt1 = new Port(defaultPortDefinition);
    n.setPort("pt1", pt1);
    Port eptActual = n.getPort("pt1");
    assertEquals(pt1, eptActual);
  }

  @Test(expected = org.manifold.intermediate.UndeclaredIdentifierException.class)
  public void testGetPort_nonexistent() throws UndeclaredIdentifierException {
    Node n = new Node(defaultNodeDefinition);
    Port ptBogus = n.getPort("bogus");
  }

  @Test
  public void testSetPort() {
    Node n = new Node(defaultNodeDefinition);
    Port pt1 = new Port(defaultPortDefinition);
    n.setPort("pt1", pt1);
  }

  @Test
  public void testSetPort_multiple_set() {
    // setting a port and then setting it again should just work
    Node n = new Node(defaultNodeDefinition);
    Port pt1 = new Port(defaultPortDefinition);
    n.setPort("pt1", pt1);
    Port pt2 = new Port(defaultPortDefinition);
    n.setPort("pt1", pt2);
  }

}
