package org.manifold.intermediate;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;
import org.manifold.intermediate.Node;
import org.manifold.intermediate.NodeType;
import org.manifold.intermediate.PortType;
import org.manifold.intermediate.Type;
import org.manifold.intermediate.Value;

public class TestValue {

  @Test
  public void testRetrieveType() {
    NodeType nDef = new NodeType(new HashMap<String, Type>(), new HashMap<String, PortType>());
    Value dom = new Node(nDef);
    Type expected = nDef;
    Type actual = dom.getType();
    assertEquals(expected, actual);
  }
  
}
