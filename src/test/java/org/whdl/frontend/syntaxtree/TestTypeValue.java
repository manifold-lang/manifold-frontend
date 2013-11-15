package org.whdl.frontend.syntaxtree;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestTypeValue {

  private TypeValue getInstance() {
     // TypeValue is abstract so we use a simple implementation.
    return BitTypeValue.getInstance();
  }

  @Test
  public void testGetType() {
    assertSame(getInstance().getType(), TypeTypeValue.getInstance());
  }
  
  @Test
  public void testGetSupertype() {
    assertSame(getInstance().getSupertype(), TypeTypeValue.getInstance());
  }

}
