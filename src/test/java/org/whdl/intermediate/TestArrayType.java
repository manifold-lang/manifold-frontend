package org.whdl.intermediate;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestArrayType {

  @Test
  public void testGetElementType() {
    Type elementType = BooleanType.getInstance();
    ArrayType aType = new ArrayType(elementType);
    assertEquals("element type not Boolean", elementType, aType.getElementType());
  }

}
