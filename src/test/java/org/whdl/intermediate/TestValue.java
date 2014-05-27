package org.whdl.intermediate;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

public class TestValue {

  @Test
  public void testRetrieveType() {
    NodeType nDef = new NodeType(new HashMap<String, Type>(), new HashMap<String, EndpointType>());
    Value dom = new Node(nDef);
    Type expected = nDef;
    Type actual = dom.getType();
    assertEquals(expected, actual);
  }
  
}
