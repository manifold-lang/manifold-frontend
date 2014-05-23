package org.whdl.intermediate;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestArrayType {

  @Test
  public void testInstantiate() throws TypeMismatchException {
    PrimitiveType elementType = new PrimitiveType(PrimitiveType.PrimitiveKind.BOOLEAN);
    ArrayType aType = new ArrayType(elementType);
    Value v = aType.instantiate();
    assertTrue("instanted value not an ArrayValue", v instanceof ArrayValue);
  }

  @Test
  public void testGetElementType() {
    PrimitiveType elementType = new PrimitiveType(PrimitiveType.PrimitiveKind.BOOLEAN);
    ArrayType aType = new ArrayType(elementType);
    assertEquals("element type not Boolean", elementType, aType.getElementType());
  }

}
