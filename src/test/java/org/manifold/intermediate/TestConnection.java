package org.manifold.intermediate;

import static org.junit.Assert.assertEquals;

import com.google.common.collect.ImmutableMap;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;


public class TestConnection {
  private static final PortType defaultPortDefinition =
      new PortType(new HashMap<>());
  private static final Type boolType = BooleanType.getInstance();
  private static final String PORT_NAME1 = "testport";
  private static final String PORT_NAME2 = "another port";
  
  private NodeType nType;
  private Node n;
  private ConnectionType conType;
  private Connection ept;
  private Value v;
  
  @Before
  public void setup() throws SchematicException {
    Map<String, PortType> portMap = ImmutableMap.of(
        PORT_NAME1, defaultPortDefinition,
        PORT_NAME2, defaultPortDefinition);
    Map<String, Map<String, Value>> portAttrMap = ImmutableMap.of(
        PORT_NAME1, ImmutableMap.of(),
        PORT_NAME2, ImmutableMap.of());
    nType = new NodeType(new HashMap<>(), portMap);
    n = new Node(nType, new HashMap<>(), portAttrMap);
    v = new BooleanValue(boolType, true);
    conType = new ConnectionType(ImmutableMap.of("v", boolType));
    
    ept = new Connection(conType, n.getPort(PORT_NAME1), n.getPort(PORT_NAME2),
        ImmutableMap.of("v", v));
  }

  @Test(expected = UndefinedBehaviourError.class)
  public void testIncorrectPortConnection() throws SchematicException {
    
    new Connection(
      conType,
      n.getPort(PORT_NAME1),
      n.getPort(PORT_NAME1),
      ImmutableMap.of("v", v)
    );
  }

  @Test
  public void testGetAttribute() throws UndeclaredAttributeException {
    assertEquals(v, ept.getAttribute("v"));
  }

  @Test(expected = org.manifold.intermediate.UndeclaredAttributeException.class)
  public void testGetAttribute_nonexistent()
      throws UndeclaredAttributeException {
    ept.getAttribute("bogus");
  }

  @Test(expected = org.manifold.intermediate.UndeclaredAttributeException.class)
  public void testCreateWithUndeclaredAttribute() throws SchematicException {
    new Connection(conType, n.getPort(PORT_NAME1), n.getPort(PORT_NAME2),
        ImmutableMap.of());
  }

  @Test(expected = org.manifold.intermediate.InvalidAttributeException.class)
  public void testCreateWithInvalidAttribute() throws SchematicException {
    Value v = new BooleanValue(boolType, true);
    new Connection(conType, n.getPort(PORT_NAME1), n.getPort(PORT_NAME2), 
        ImmutableMap.of("v", v, "bogus", v));
  }
  
  @Test
  public void testGetPort() throws UndeclaredIdentifierException {
    assertEquals(n.getPort(PORT_NAME1), ept.getFrom());
    assertEquals(n.getPort(PORT_NAME2), ept.getTo());
  }
}
