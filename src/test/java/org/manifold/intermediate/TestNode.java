package org.manifold.intermediate;

import static org.junit.Assert.assertEquals;

import com.google.common.collect.ImmutableMap;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestNode {

  private static final PortType defaultPortDefinition =
      new PortType(new HashMap<>());
  private static final Type boolType = BooleanType.getInstance();
  private static final String PORT_NAME = "testport";
  private static final String PORT_ATTR_KEY = "the truth will set you free";
  private static final Map<String, Map<String, Value>> PORT_ATTRS =
      ImmutableMap.of(PORT_NAME, ImmutableMap.of());
  
  private NodeType hasNoAttrs;
  private NodeType hasABCNodeAttr;
  
  @Before
  public void setup() {
    Map<String, PortType> portMap =
        ImmutableMap.of(PORT_NAME, defaultPortDefinition);
    hasNoAttrs = new NodeType(new HashMap<>(), portMap);
    hasABCNodeAttr = new NodeType(ImmutableMap.of("abc", boolType), portMap);
  }

  @Test(
      expected = org.manifold.intermediate.InvalidIdentifierException.class)
  public void testCreateWithMissingPort() throws SchematicException {
    Value v = new BooleanValue(boolType, true);
    new Node(hasABCNodeAttr, ImmutableMap.of("abc", v), new HashMap<>());
  }
  
  @Test(
      expected = org.manifold.intermediate.UndeclaredIdentifierException.class)
  public void testCreateWithInvalidPortName() throws SchematicException {
    Value v = new BooleanValue(boolType, true);
    Map<String, Map<String, Value>> portAttrMap = ImmutableMap.of(
        PORT_NAME, new HashMap<>(),
        "bogusPort", new HashMap<>());
    new Node(hasABCNodeAttr, ImmutableMap.of("abc", v), portAttrMap);
  }

  @Test
  public void testGetAttribute() throws SchematicException {
    Value v = new BooleanValue(boolType, true);
    Node n = new Node(hasABCNodeAttr, ImmutableMap.of("abc", v), PORT_ATTRS);
    assertEquals(v, n.getAttribute("abc"));
  }

  @Test(expected = org.manifold.intermediate.UndeclaredAttributeException.class)
  public void testGetAttribute_nonexistent() throws SchematicException {
    Node n = new Node(hasNoAttrs, new HashMap<>(), PORT_ATTRS);
    n.getAttribute("bogus");
  }

  @Test
  public void testGetPort() throws SchematicException {
    Node n = new Node(hasNoAttrs, new HashMap<>(), PORT_ATTRS);
    Port port = n.getPort(PORT_NAME);
    assertEquals(defaultPortDefinition, port.getType());
  }

  @Test(expected = UndeclaredIdentifierException.class)
  public void testGetPort_nonexistent() throws SchematicException {
    Node n = new Node(hasNoAttrs, new HashMap<>(), PORT_ATTRS);
    n.getPort("bogus");
  }

  @Test
  public void testPortsWithValidAttributes() throws SchematicException {
    PortType portTypeWithAttr =
        new PortType(ImmutableMap.of(PORT_ATTR_KEY, boolType));
    Map<String, PortType> portTypeMap =
        ImmutableMap.of(PORT_NAME, portTypeWithAttr);
    NodeType withPortAttrs = new NodeType(ImmutableMap.of(), portTypeMap);
    BooleanValue v = new BooleanValue(boolType, false);
    Map<String, Map<String, Value>> portAttrMap = ImmutableMap.of(PORT_NAME,
        ImmutableMap.of(PORT_ATTR_KEY, v));
    Node n = new Node(withPortAttrs, ImmutableMap.of(), portAttrMap);
    assertEquals(v, n.getPort(PORT_NAME).getAttribute(PORT_ATTR_KEY));
  }
}
