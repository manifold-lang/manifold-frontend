package org.whdl.intermediate;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;

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

  @Test(expected = org.whdl.intermediate.UndeclaredAttributeException.class)
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
