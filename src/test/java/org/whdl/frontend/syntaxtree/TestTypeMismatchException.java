package org.whdl.frontend.syntaxtree;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestTypeMismatchException {

  public TypeValue getTypeValueInstance(){
    return BitTypeValue.getInstance();
  }
  
  public Value getValueInstance(){
    return BitValue.getInstance(false);
  }
  
  public TypeMismatchException getInstance(){
    return new TypeMismatchException(getTypeValueInstance(), getValueInstance());
  }
  
  @Test
  public void testGetMessage_containsTypeNames() {
    TypeMismatchException instance = getInstance();
    String msg = instance.getMessage();
    assertTrue(msg.contains(getTypeValueInstance().toString()));
    assertTrue(msg.contains(getValueInstance().toString()));
  }

}
