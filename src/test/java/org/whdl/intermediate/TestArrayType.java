package org.whdl.intermediate;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestArrayType {

  @Test
  public void testInstantiate() {
    PrimitiveType elementType = new PrimitiveType(PrimitiveType.PrimitiveKind.BOOLEAN);
    ArrayType aType = new ArrayType(elementType);
    Value v = aType.instantiate("foo");
    assertTrue("instanted value not an ArrayValue", v instanceof ArrayValue);
  }

  @Test
  public void testGetElementType() {
    PrimitiveType elementType = new PrimitiveType(PrimitiveType.PrimitiveKind.BOOLEAN);
    ArrayType aType = new ArrayType(elementType);
    assertEquals("element type not Boolean", elementType, aType.getElementType());
  }

  @Test
  public void testEquals_true() {
    // two ArrayTypes are equal if their element types are equal
    PrimitiveType elementType = new PrimitiveType(PrimitiveType.PrimitiveKind.BOOLEAN);
    ArrayType aType1 = new ArrayType(elementType);
    ArrayType aType2 = new ArrayType(elementType);
    assertEquals(aType1, aType2);
  }
  
  @Test
  public void testEquals_false() {
    // two ArrayTypes are equal if their element types are equal
    PrimitiveType elementType1 = new PrimitiveType(PrimitiveType.PrimitiveKind.BOOLEAN);
    PrimitiveType elementType2 = new PrimitiveType(PrimitiveType.PrimitiveKind.INTEGER);
    ArrayType aType1 = new ArrayType(elementType1);
    ArrayType aType2 = new ArrayType(elementType2);
    assertNotEquals(aType1, aType2);
  }

}
