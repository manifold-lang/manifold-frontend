package org.whdl.frontend.syntaxtree;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestTypeValue {

  private TypeValue getInstance() {
     // TypeValue is abstract so we use a simple implementation.
    return BitTypeValue.getInstance();
  }

  @Test
  public void testGetTypeValue() {
    assertSame(getInstance().getTypeValue(), TypeTypeValue.getInstance());
  }
  
  @Test
  public void testGetSupertypeTypeValue() {
    assertSame(getInstance().getSupertypeTypeValue(), TypeTypeValue.getInstance());
  }

}
