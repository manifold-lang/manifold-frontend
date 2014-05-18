package org.whdl.intermediate;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestValue {

  @Test
  public void testRetrieveType() {
    NodeTypeDefinition nDef = new NodeTypeDefinition("nod");
    Value dom = new Node(nDef);
    Type expected = new NodeType(nDef);
    Type actual = dom.getType();
    assertEquals(expected, actual);
  }
  
}
