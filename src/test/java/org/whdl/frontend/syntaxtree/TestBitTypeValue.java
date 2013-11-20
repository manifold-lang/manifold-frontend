package org.whdl.frontend.syntaxtree;

import static org.junit.Assert.*;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.instanceOf;

public class TestBitTypeValue {

  private BitTypeValue getInstance() {
    return BitTypeValue.getInstance();
  }
  
  @Test
  public void testStaticGetInstance() {
    assertThat(
      BitTypeValue.getInstance(),
      instanceOf(BitTypeValue.class)
    );
  }
  
  @Test
  public void testVerify() {
    getInstance().verify();
  }


}
