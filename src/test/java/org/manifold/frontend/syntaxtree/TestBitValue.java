package org.manifold.frontend.syntaxtree;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import org.junit.Test;

public class TestBitValue {
  
  private BitValue getInstance(boolean value) {
    return BitValue.getInstance(value);
  }

  @Test
  public void testStaticGetInstance() {
    assertThat(BitValue.getInstance(true), instanceOf(BitValue.class));
    assertThat(BitValue.getInstance(false), instanceOf(BitValue.class));
  }

  @Test
  public void testToBoolean() {
    assertTrue(getInstance(true).toBoolean());
    assertFalse(getInstance(false).toBoolean());
  }

  @Test
  public void testGetType() {
    assertThat(
      getInstance(true).getType(),
      instanceOf(BitTypeValue.class)
    );
    assertThat(
      getInstance(false).getType(),
      instanceOf(BitTypeValue.class)
    );
  }
  
  @Test
  public void testIsCompiletimeEvaluable() {
    assertTrue(getInstance(false).isCompiletimeEvaluable());
  }
  
  @Test
  public void testIsSynthesizable() {
    assertTrue(getInstance(false).isSynthesizable());
  }
  
  @Test
  public void testVerify() {
    getInstance(true).verify();
  }

}
