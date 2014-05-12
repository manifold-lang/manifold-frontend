package org.whdl.intermediate;

import static org.junit.Assert.*;

import org.junit.Test;
import org.whdl.intermediate.PrimitiveType.PrimitiveKind;

public class TestPrimitiveType {

  @Test
  public void testInstantiateBoolean() {
    PrimitiveType pType = new PrimitiveType(PrimitiveKind.BOOLEAN);
    Value v = pType.instantiate("foo");
    assertTrue("instantiated value not a BooleanValue", v instanceof BooleanValue);
  }
  
  @Test
  public void testInstantiateInteger() {
    PrimitiveType pType = new PrimitiveType(PrimitiveKind.INTEGER);
    Value v = pType.instantiate("foo");
    assertTrue("instantiated value not an IntegerValue", v instanceof IntegerValue);
  }
  
  @Test
  public void testInstantiateString() {
    PrimitiveType pType = new PrimitiveType(PrimitiveKind.STRING);
    Value v = pType.instantiate("foo");
    assertTrue("instantiated value not a StringValue", v instanceof StringValue);
  }

  @Test
  public void testEqualsObject_true() {
    // two PrimitiveTypes are equal if their kinds are equal
    PrimitiveType p1 = new PrimitiveType(PrimitiveKind.INTEGER);
    PrimitiveType p2 = new PrimitiveType(PrimitiveKind.INTEGER);
    assertEquals(p1, p2);
  }
  
  @Test
  public void testEqualsObject_false() {
    // two PrimitiveTypes are equal if their kinds are equal
    PrimitiveType p1 = new PrimitiveType(PrimitiveKind.INTEGER);
    PrimitiveType p2 = new PrimitiveType(PrimitiveKind.BOOLEAN);
    assertNotEquals(p1, p2);
  }

}
