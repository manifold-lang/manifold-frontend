package org.manifold.intermediate;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.HashMap;

public class TestValue {

  @Test
  public void testRetrieveType() {
    NodeType nDef = new NodeType(new HashMap<>(), new HashMap<>());
    Value dom = new Node(nDef);
    Type expected = nDef;
    Type actual = dom.getType();
    assertEquals(expected, actual);
  }
  
}
