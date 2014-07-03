package org.manifold.intermediate;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public class TestPort {

  private static final Type boolType = BooleanType.getInstance();
  private static final PortType defaultPortType = new PortType(ImmutableMap.of("v", boolType));
  private static final String PORT_NAME = "testport";
  private static final Value v = new BooleanValue(boolType, true);
  
  private Node parent;
  private Port port;
  
  @Before
  public void setup() throws UndeclaredIdentifierException, UndeclaredAttributeException {
    HashMap<String, PortType> portTypeMap = new HashMap<>();
    portTypeMap.put(PORT_NAME, defaultPortType);
    
    Map<String, Map<String, Value>> portMap = new HashMap<>();
    portMap.put(PORT_NAME, ImmutableMap.of("v", v));
    NodeType parentType = new NodeType(new HashMap<>(), portTypeMap);
    parent = new Node(parentType, new HashMap<>(), portMap);
    port = parent.getPort(PORT_NAME);
  }

  @Test
  public void testGetAttribute() throws UndeclaredAttributeException {
    assertEquals(v, port.getAttribute("v"));
  }

  @Test(expected = org.manifold.intermediate.UndeclaredAttributeException.class)
  public void testGetAttribute_nonexistent()
      throws UndeclaredAttributeException {
    port.getAttribute("bogus");
  }
  
  @Test
  public void testGetParent() throws UndeclaredIdentifierException {
    assertEquals(parent, parent.getPort(PORT_NAME).getParent());
  }
}
