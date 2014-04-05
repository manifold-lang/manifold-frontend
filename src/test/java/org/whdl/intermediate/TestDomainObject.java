package org.whdl.intermediate;

import static org.junit.Assert.*;

import org.junit.Test;
import org.whdl.intermediate.definitions.NodeDefinition;
import org.whdl.intermediate.exceptions.UndefinedBehaviourError;
import org.whdl.intermediate.types.NodeType;

public class TestDomainObject {

  @Test
  public void testSetAndRetrieveType() {
    NodeDefinition nDef = new NodeDefinition("nod");
    DomainObject dom = new Node("nod-1", nDef);
    Type expected = new NodeType("nod");
    dom.setType(expected);
    Type actual = dom.getType();
    assertEquals(expected, actual);
  }

  @Test(expected=UndefinedBehaviourError.class)
  public void testSetTypeToNull_undefined(){
    NodeDefinition nDef = new NodeDefinition("nod");
    DomainObject dom = new Node("nod-1", nDef);
    dom.setType(null);
  }
  
  @Test(expected=UndefinedBehaviourError.class)
  public void testGetTypeBeforeSet_undefined(){
    NodeDefinition nDef = new NodeDefinition("nod");
    DomainObject dom = new Node("nod-1", nDef);
    
    Type bogus = dom.getType();
  }
  
  @Test(expected = UndefinedBehaviourError.class)
  public void testSetTypeTwice_undefined(){
    NodeDefinition nDef = new NodeDefinition("nod");
    DomainObject dom = new Node("nod-1", nDef);
    
    Type expected = new NodeType("nod");
    try{
      dom.setType(expected);
    }catch(UndefinedBehaviourError e){
      fail("exception thrown too early: " + e.getMessage());
    }
    dom.setType(expected);
  }
  
}
