package org.whdl.frontend.syntaxtree;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestTypeValue {
  
  // FIXME(lucas) Replace this with another real type value class as soon as
  // we have one other than BitTypeValue and TypeTypeValue
  static private class FacadeTypeValue extends TypeValue {

    private final static FacadeTypeValue instance = new FacadeTypeValue();

    public static FacadeTypeValue getInstance() {
      return instance;
    }

    private FacadeTypeValue() {}

    public void verify() {}
  }

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
  
  @Test
  public void isSubtypeOf_equal() {
    assertTrue(getInstance().isSubtypeOf(getInstance()));
  }
  
  @Test
  public void isSubtypeOf_subtype() {
    assertTrue(BitTypeValue.getInstance().isSubtypeOf(
        TypeTypeValue.getInstance()));
  }
  
  @Test
  public void isSubtypeOf_false() {
    assertFalse(BitTypeValue.getInstance().isSubtypeOf(
        FacadeTypeValue.getInstance()));
  }
}
