package org.whdl.intermediate;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestValue {

  @Test
  public void testRetrieveType() {
    NodeTypeDefinition nDef = new NodeTypeDefinition("nod");
    Value dom = new Node("nod-1", nDef);
    Type expected = new NodeType(nDef);
    Type actual = dom.getType();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetInstanceName(){
    NodeTypeDefinition nDef = new NodeTypeDefinition("nod");
    String expected = "nod-1";
    Value dom = new Node(expected, nDef);
    String actual = dom.getInstanceName();
    assertEquals(expected, actual);
  }
  
}
