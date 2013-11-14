package org.whdl.frontend.syntaxtree;

import static org.junit.Assert.*;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.instanceOf;

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
}
