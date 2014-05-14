package org.whdl.intermediate;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestConnection {

  private static final ConnectionTypeDefinition defaultConnectionDefinition = new ConnectionTypeDefinition("foo");
  
  @Test
  public void testGetDefinition() {
    Connection ept = new Connection("foo-1", defaultConnectionDefinition);
    assertEquals(defaultConnectionDefinition, ept.getDefinition());
  }

  @Test
  public void testGetAttribute() throws UndeclaredIdentifierException {
    Connection ept = new Connection("foo-1", defaultConnectionDefinition);
    Value v = new BooleanValue("v", true);
    ept.setAttribute("v", v);
    Value vActual = ept.getAttribute("v");
    assertEquals(v, vActual);
  }

  @Test(expected=org.whdl.intermediate.UndeclaredIdentifierException.class)
  public void testGetAttribute_nonexistent() throws UndeclaredIdentifierException {
    Connection ept = new Connection("foo-1", defaultConnectionDefinition);
    Value vBogus = ept.getAttribute("bogus");
  }
  
  @Test
  public void testSetAttribute() {
    Connection ept = new Connection("foo-1", defaultConnectionDefinition);
    Value v = new BooleanValue("v", true);
    ept.setAttribute("v", v);
  }
  
  @Test
  public void testSetAttribute_multiple_set() {
    // setting an attribute twice should just work
    Connection ept = new Connection("foo-1", defaultConnectionDefinition);
    Value v = new BooleanValue("v", true);
    ept.setAttribute("v", v);
    Value v2 = new BooleanValue("v", false);
    ept.setAttribute("v", v2);
  }

}
