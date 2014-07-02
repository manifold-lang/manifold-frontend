package org.manifold.frontend.syntaxtree;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import org.junit.Test;

public class TestTypeTypeValue {

  public TypeTypeValue getInstance() {
    return TypeTypeValue.getInstance();
  }

  @Test
  public void testStaticGetInstance() {
    assertThat(TypeTypeValue.getInstance(), instanceOf(TypeTypeValue.class));
  }

  @Test
  public void testGetType() {
    assertThat(getInstance().getType(), instanceOf(TypeTypeValue.class));
  }
  
  @Test
  public void isSubtypeOf_false() {
    assertFalse(getInstance().isSubtypeOf(BitTypeValue.getInstance()));
  }
  
  @Test
  public void isSubtypeOf_true() {
    assertTrue(getInstance().isSubtypeOf(getInstance()));
  }
  
  @Test
  public void verify() {
    getInstance().verify();
  }
  
  @Test
  public void testVerify() {
    getInstance().verify();
  }

}
