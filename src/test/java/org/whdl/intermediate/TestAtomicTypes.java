package org.whdl.intermediate;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestAtomicTypes {
  
  @Test
  public void testEquality() {
    Type p1 = new BooleanType();
    Type p2 = new IntegerType();
    // two types are equal iff they are the same object
    assertEquals(p1, p1);
    assertEquals(p2, p2);
    assertNotEquals(p1, p2);
    Type p3 = new BooleanType();
    assertNotEquals(p1, p3);
  }

}
