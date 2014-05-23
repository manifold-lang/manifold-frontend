package org.whdl.intermediate;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestValue {

  @Test
  public void testRetrieveType() {
    NodeType nDef = new NodeType();
    Value dom = new Node(nDef);
    Type expected = nDef;
    Type actual = dom.getType();
    assertEquals(expected, actual);
  }
  
}
