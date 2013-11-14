package org.whdl.frontend.syntaxtree;

import static org.junit.Assert.*;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.instanceOf;

public class TestBitTypeValue {

  @Test
  public void testStaticGetInstance() {
    assertThat(
      BitTypeValue.getInstance(),
      instanceOf(BitTypeValue.class)
    );
  }

}
