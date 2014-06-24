package org.manifold.intermediate;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;
import org.manifold.intermediate.BooleanType;
import org.manifold.intermediate.BooleanValue;
import org.manifold.intermediate.Connection;
import org.manifold.intermediate.ConnectionType;
import org.manifold.intermediate.Type;
import org.manifold.intermediate.UndeclaredAttributeException;
import org.manifold.intermediate.Value;

public class TestConnection {

  private static final ConnectionType defaultConnectionDefinition = new ConnectionType(new HashMap<String, Type>());
  private static final Type boolType = BooleanType.getInstance();

  @Test
  public void testGetAttribute() throws UndeclaredAttributeException {
    Connection ept = new Connection(defaultConnectionDefinition);
    Value v = new BooleanValue(boolType, true);
    ept.setAttribute("v", v);
    Value vActual = ept.getAttribute("v");
    assertEquals(v, vActual);
  }

  @Test(expected = org.manifold.intermediate.UndeclaredAttributeException.class)
  public void testGetAttribute_nonexistent() throws UndeclaredAttributeException {
    Connection ept = new Connection(defaultConnectionDefinition);
    Value vBogus = ept.getAttribute("bogus");
  }

  @Test
  public void testSetAttribute() {
    Connection ept = new Connection(defaultConnectionDefinition);
    Value v = new BooleanValue(boolType, true);
    ept.setAttribute("v", v);
  }

  @Test
  public void testSetAttribute_multiple_set() {
    // setting an attribute twice should just work
    Connection ept = new Connection(defaultConnectionDefinition);
    Value v = new BooleanValue(boolType, true);
    ept.setAttribute("v", v);
    Value v2 = new BooleanValue(boolType, false);
    ept.setAttribute("v", v2);
  }

}
