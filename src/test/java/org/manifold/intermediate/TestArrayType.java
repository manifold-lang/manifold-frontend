package org.manifold.intermediate;

import static org.junit.Assert.*;

import org.junit.Test;
import org.manifold.intermediate.ArrayType;
import org.manifold.intermediate.BooleanType;
import org.manifold.intermediate.Type;

public class TestArrayType {

  @Test
  public void testGetElementType() {
    Type elementType = BooleanType.getInstance();
    ArrayType aType = new ArrayType(elementType);
    assertEquals("element type not Boolean", elementType, aType.getElementType());
  }

}
