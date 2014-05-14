package org.whdl.intermediate;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestNodeTypeDefinition {

  @Test
  public void testInstantiate_no_attributes() {
    NodeTypeDefinition nodeTypeDef = new NodeTypeDefinition("foo");
    Value node = nodeTypeDef.instantiate("con");
    // the Value we receive must be a Node
    assertTrue("instanted value is not a Node", node instanceof Node);
  }

  @Test
  public void testInstantiate_with_attributes() throws UndeclaredIdentifierException {
    NodeTypeDefinition nodeTypeDef = new NodeTypeDefinition("foo");
    // add one simple attribute
    TypeTypeDefinition attrTTD = new TypeTypeDefinition("attr-type", new PrimitiveType(PrimitiveType.PrimitiveKind.BOOLEAN));
    nodeTypeDef.addAttribute("attr", attrTTD);
    
    Node node = (Node)nodeTypeDef.instantiate("ept");
    Value attr = node.getAttribute("attr"); // it is sufficient that we return /something/ without throwing an exception
  }
  
}
