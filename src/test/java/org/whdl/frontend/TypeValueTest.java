package org.whdl.frontend;

import static org.junit.Assert.*;
import org.junit.Test;

public class TypeValueTest {

  private TypeValue getInstance() {
     // TypeValue is abstract so we use a simple implementation.
    return BooleanTypeValue.getInstance();
  }

  @Test
  public void testGetTypeValue() {
    assertSame(getInstance().getTypeValue(), TypeTypeValue.getInstance());
  }

}
