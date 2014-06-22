package org.manifold.intermediate;

import static org.junit.Assert.*;

import org.junit.Test;
import org.manifold.intermediate.BooleanType;
import org.manifold.intermediate.IntegerType;
import org.manifold.intermediate.StringType;

public class TestAtomicTypes {
  
  @Test
  public void testEquality() {
    BooleanType p1 = BooleanType.getInstance();
    IntegerType p2 = IntegerType.getInstance();
    StringType p3 = StringType.getInstance();
    // Primitive types are singletons.
    assertEquals(p1, BooleanType.getInstance());
    assertEquals(p2, IntegerType.getInstance());
    assertEquals(p3, StringType.getInstance());
    // Two types are equal iff they are the same object
    assertNotEquals(p1, p2);
    assertNotEquals(p1, p3);
    assertNotEquals(p2, p3);
    // Equality function doesn't fail for null.
    assertFalse(p1.equals(null));
  }

}
