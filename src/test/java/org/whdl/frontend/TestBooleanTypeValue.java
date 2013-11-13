package org.whdl.frontend;

import static org.junit.Assert.*;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.instanceOf;

public class TestBooleanTypeValue {

  @Test
  public void testStaticGetInstance() {
    assertThat(BooleanTypeValue.getInstance(),
               instanceOf(BooleanTypeValue.class));
  }

}
