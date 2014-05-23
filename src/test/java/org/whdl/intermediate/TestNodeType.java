package org.whdl.intermediate;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestNodeType {

  @Test
  public void testInstantiate_no_attributes() {
    NodeType nodeTypeDef = new NodeType();
    Value node = nodeTypeDef.instantiate();
    // the Value we receive must be a Node
    assertTrue("instanted value is not a Node", node instanceof Node);
  }

  @Test
  public void testInstantiate_with_attributes() throws UndeclaredIdentifierException {
    NodeType nodeTypeDef = new NodeType();
    // add one simple attribute
    UserDefinedType attrTTD = new UserDefinedType(new PrimitiveType(PrimitiveType.PrimitiveKind.BOOLEAN));
    nodeTypeDef.addAttribute("attr", attrTTD);
    
    Node node = (Node)nodeTypeDef.instantiate();
    Value attr = node.getAttribute("attr"); // it is sufficient that we return /something/ without throwing an exception
  }
  
}
