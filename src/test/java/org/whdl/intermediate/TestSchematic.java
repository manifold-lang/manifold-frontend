package org.whdl.intermediate;

import static org.junit.Assert.*;

import org.junit.Test;
import org.whdl.intermediate.exceptions.*;
import org.whdl.intermediate.types.*;

public class TestSchematic {

  @Test
  public void testAddTypeDef() throws MultipleDefinitionException {
    Schematic s = new Schematic("test");
    TypeTypeDefinition t1 = new TypeTypeDefinition("foo", new PrimitiveType(PrimitiveType.PrimitiveKind.INTEGER));
    s.addTypeTypeDefinition(t1);
  }
  
  @Test(expected = MultipleDefinitionException.class)
  public void testAddTypeDef_multipleDefinitions() throws MultipleDefinitionException{
    // We should not be able to add two type definitions whose first argument is the same string.
    Schematic s = new Schematic("test");
    try{
      TypeTypeDefinition t1 = new TypeTypeDefinition("foo", new PrimitiveType(PrimitiveType.PrimitiveKind.INTEGER));
      s.addTypeTypeDefinition(t1);
    }catch(MultipleDefinitionException mde){
      fail("exception thrown too early: " + mde.getMessage());
    }
    TypeTypeDefinition t2 = new TypeTypeDefinition("foo", new PrimitiveType(PrimitiveType.PrimitiveKind.STRING));
    s.addTypeTypeDefinition(t2);
  }
  
  @Test(expected = MultipleDefinitionException.class)
  public void testAddTypeDef_maskDefaultType() throws MultipleDefinitionException{
    // Suppose we create a new Schematic and then try to redefine the meaning of "Int".
    // Since "Int" is a built-in type, this should result in a MultipleDefinitionException being thrown.
    Schematic s = new Schematic("test");
    TypeTypeDefinition td = new TypeTypeDefinition("Int", new PrimitiveType(PrimitiveType.PrimitiveKind.STRING));
    s.addTypeTypeDefinition(td);
  }
  
  @Test
  public void testAddEndpointDef() throws MultipleDefinitionException {
    Schematic s = new Schematic("test");
    EndpointTypeDefinition e1 = new EndpointTypeDefinition("n1");
    s.addEndpointTypeDefinition(e1);
  }
  
  @Test(expected=MultipleDefinitionException.class)
  public void testAddEndpointDef_multipleDefinitions() throws MultipleDefinitionException {
    // We should not be able to add two endpoint definitions whose first argument is the same string.
    Schematic s = new Schematic("test");
    try{
      EndpointTypeDefinition n1 = new EndpointTypeDefinition("foo");
      s.addEndpointTypeDefinition(n1);
    }catch(MultipleDefinitionException mde){
      fail("exception thrown too early: " + mde.getMessage());
    }
    EndpointTypeDefinition n2 = new EndpointTypeDefinition("foo");
    s.addEndpointTypeDefinition(n2);
  }
  
  @Test
  public void testAddNodeDef() throws MultipleDefinitionException {
    Schematic s = new Schematic("test");
    NodeTypeDefinition n1 = new NodeTypeDefinition("n1");
    s.addNodeTypeDefinition(n1);
  }
  
  @Test(expected=MultipleDefinitionException.class)
  public void testAddNodeDef_multipleDefinitions() throws MultipleDefinitionException {
    // We should not be able to add two node definitions whose first argument is the same string.
    Schematic s = new Schematic("test");
    try{
      NodeTypeDefinition n1 = new NodeTypeDefinition("foo");
      s.addNodeTypeDefinition(n1);
    }catch(MultipleDefinitionException mde){
      fail("exception thrown too early: " + mde.getMessage());
    }
    NodeTypeDefinition n2 = new NodeTypeDefinition("foo");
    s.addNodeTypeDefinition(n2);
  }
  
  @Test
  public void testAddConnectionDef() throws MultipleDefinitionException {
    Schematic s = new Schematic("test");
    ConnectionTypeDefinition c1 = new ConnectionTypeDefinition("c1");
    s.addConnectionTypeDefinition(c1);
  }
  
  @Test(expected=MultipleDefinitionException.class)
  public void testAddConnectionDef_multipleDefinitions() throws MultipleDefinitionException {
    // We should not be able to add two connection definitions whose first argument is the same string.
    Schematic s = new Schematic("test");
    try{
      ConnectionTypeDefinition c1 = new ConnectionTypeDefinition("foo");
      s.addConnectionTypeDefinition(c1);
    }catch(MultipleDefinitionException mde){
      fail("exception thrown too early: " + mde.getMessage());
    }
    ConnectionTypeDefinition c2 = new ConnectionTypeDefinition("foo");
    s.addConnectionTypeDefinition(c2);
  }
  
  @Test
  public void testAddConstraintDef() throws MultipleDefinitionException {
    Schematic s = new Schematic("test");
    ConstraintTypeDefinition e1 = new ConstraintTypeDefinition("c1");
    s.addConstraintTypeDefinition(e1);
  }
  
  @Test(expected=MultipleDefinitionException.class)
  public void testAddConstraintDef_multipleDefinitions() throws MultipleDefinitionException {
    // We should not be able to add two constraint definitions whose first argument is the same string.
    Schematic s = new Schematic("test");
    try{
      ConstraintTypeDefinition e1 = new ConstraintTypeDefinition("foo");
      s.addConstraintTypeDefinition(e1);
    }catch(MultipleDefinitionException mde){
      fail("exception thrown too early: " + mde.getMessage());
    }
    ConstraintTypeDefinition e2 = new ConstraintTypeDefinition("foo");
    s.addConstraintTypeDefinition(e2);
  }

  @Test
  public void testSeparationOfNamespaces_DomainObjects() throws MultipleDefinitionException{
    // We should be able to add one of each of a TypeDefinition, ConstraintDefinition,
    // ConnectionDefinition, NodeDefinition, and EndpointDefinition with the same name
    // without encountering a "multiple definition" exception.
    Schematic s = new Schematic("test");
    
    TypeTypeDefinition t1 = new TypeTypeDefinition("foo", new PrimitiveType(PrimitiveType.PrimitiveKind.STRING));
    ConstraintTypeDefinition ct1 = new ConstraintTypeDefinition("foo");
    ConnectionTypeDefinition cn1 = new ConnectionTypeDefinition("foo");
    NodeTypeDefinition n1 = new NodeTypeDefinition("foo");
    EndpointTypeDefinition e1 = new EndpointTypeDefinition("foo");
    
    s.addTypeTypeDefinition(t1);
    s.addConstraintTypeDefinition(ct1);
    s.addConnectionTypeDefinition(cn1);
    s.addNodeTypeDefinition(n1);
    s.addEndpointTypeDefinition(e1);
  }
  
}
