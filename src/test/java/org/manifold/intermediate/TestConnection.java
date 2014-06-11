package org.manifold.intermediate;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class TestConnection {
  private static final PortType defaultPortDefinition = 
      new PortType(new HashMap<>());
  private static final Type boolType = BooleanType.getInstance();
  private static final String PORT_NAME1 = "testport";
  private static final String PORT_NAME2 = "another port";
  
  private Node n;
  private ConnectionType conType;
  private Connection ept;
  
  @Before
  public void setup() throws UndeclaredIdentifierException {
    HashMap<String, PortType> portMap = new HashMap<>();
    portMap.put(PORT_NAME1, defaultPortDefinition);
    portMap.put(PORT_NAME2, defaultPortDefinition);
    n = new Node(new NodeType(new HashMap<>(), portMap));
    
    conType = new ConnectionType(new HashMap<>());
    ept = new Connection(conType, n.getPort(PORT_NAME1), n.getPort(PORT_NAME2));
  }

  @Test(expected = UndefinedBehaviourError.class)
  public void testIncorrectPortConnection()
      throws UndefinedBehaviourError, UndeclaredIdentifierException {
    
    Connection con = new Connection(
      conType,
      n.getPort(PORT_NAME1),
      n.getPort(PORT_NAME1)
    );
  }

  @Test
  public void testGetAttribute() throws UndeclaredAttributeException {
    Value v = new BooleanValue(boolType, true);
    ept.setAttribute("v", v);
    Value vActual = ept.getAttribute("v");
    assertEquals(v, vActual);
  }

  @Test(expected = org.manifold.intermediate.UndeclaredAttributeException.class)
  public void testGetAttribute_nonexistent()
      throws UndeclaredAttributeException {
    Value vBogus = ept.getAttribute("bogus");
  }

  @Test
  public void testSetAttribute() {
    Value v = new BooleanValue(boolType, true);
    ept.setAttribute("v", v);
  }

  @Test
  public void testSetAttribute_multiple_set() {
    // setting an attribute twice should just work
    Value v = new BooleanValue(boolType, true);
    ept.setAttribute("v", v);
    Value v2 = new BooleanValue(boolType, false);
    ept.setAttribute("v", v2);
  }

  @Test
  public void testGetPort() throws UndeclaredIdentifierException {
    assertEquals(n.getPort(PORT_NAME1), ept.getFrom());
    assertEquals(n.getPort(PORT_NAME2), ept.getTo());
  }
}
