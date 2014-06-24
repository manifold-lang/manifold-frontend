package org.manifold.intermediate;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;
import org.manifold.intermediate.BooleanType;
import org.manifold.intermediate.BooleanValue;
import org.manifold.intermediate.Port;
import org.manifold.intermediate.PortType;
import org.manifold.intermediate.Type;
import org.manifold.intermediate.UndeclaredAttributeException;
import org.manifold.intermediate.Value;

public class TestPort {

  private static final PortType defaultPortType = new PortType(new HashMap<String, Type>());
  private static final Type boolType = BooleanType.getInstance();

  @Test
  public void testGetAttribute() throws UndeclaredAttributeException {
    Port port = new Port(defaultPortType);
    Value v = new BooleanValue(boolType, true);
    port.setAttribute("v", v);
    Value vActual = port.getAttribute("v");
    assertEquals(v, vActual);
  }

  @Test(expected = org.manifold.intermediate.UndeclaredAttributeException.class)
  public void testGetAttribute_nonexistent() throws UndeclaredAttributeException {
    Port port = new Port(defaultPortType);
    Value vBogus = port.getAttribute("bogus");
  }

  @Test
  public void testSetAttribute() {
    Port port = new Port(defaultPortType);
    Value v = new BooleanValue(boolType, true);
    port.setAttribute("v", v);
  }

  @Test
  public void testSetAttribute_multiple_set() {
    // setting an attribute twice should just work
    Port port = new Port(defaultPortType);
    Value v = new BooleanValue(boolType, true);
    port.setAttribute("v", v);
    Value v2 = new BooleanValue(boolType, false);
    port.setAttribute("v", v2);
  }

}
