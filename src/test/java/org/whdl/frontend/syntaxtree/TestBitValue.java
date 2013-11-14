package org.whdl.frontend.syntaxtree;

import static org.junit.Assert.*;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.instanceOf;

public class TestBooleanValue {
  
  private BooleanValue getInstance(boolean value) {
    return BooleanValue.getInstance(value);
  }

  @Test
  public void testStaticGetInstance() {
    assertThat(BooleanValue.getInstance(true), instanceOf(BooleanValue.class));
    assertThat(BooleanValue.getInstance(false), instanceOf(BooleanValue.class));
  }

  @Test
  public void testToBoolean() {
    assertTrue(getInstance(true).toBoolean());
    assertFalse(getInstance(false).toBoolean());
  }

  @Test
  public void testGetTypeValue() {
    assertThat(getInstance(true).getTypeValue(),
               instanceOf(BooleanTypeValue.class));
    assertThat(getInstance(false).getTypeValue(),
               instanceOf(BooleanTypeValue.class));
  }

}
