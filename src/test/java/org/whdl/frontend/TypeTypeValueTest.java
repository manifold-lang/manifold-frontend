package org.whdl.frontend;

import static org.junit.Assert.*;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.instanceOf;

public class TypeTypeValueTest {

  public TypeTypeValue getInstance() {
    return TypeTypeValue.getInstance();
  }

  @Test
  public void testStaticGetInstance() {
    assertThat(TypeTypeValue.getInstance(), instanceOf(TypeTypeValue.class));
  }

  @Test
  public void testGetTypeValue() {
    assertThat(getInstance().getTypeValue(), instanceOf(TypeTypeValue.class));
  }

}
