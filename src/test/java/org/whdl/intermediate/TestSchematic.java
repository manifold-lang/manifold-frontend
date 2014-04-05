package org.whdl.intermediate;

import static org.junit.Assert.*;

import org.junit.Test;
import org.whdl.intermediate.definitions.*;
import org.whdl.intermediate.exceptions.*;
import org.whdl.intermediate.types.*;

public class TestSchematic {

  @Test
  public void testAddTypeDef() throws MultipleDefinitionException {
    Schematic s = new Schematic("test");
    TypeDefinition t1 = new TypeDefinition("foo", PrimitiveType.INTEGER);
    s.addTypeDefinition(t1);
  }
  
  @Test(expected = MultipleDefinitionException.class)
  public void testAddTypeDef_multipleDefinitions() throws MultipleDefinitionException{
    // We should not be able to add two type definitions whose first argument is the same string.
    Schematic s = new Schematic("test");
    try{
      TypeDefinition t1 = new TypeDefinition("foo", PrimitiveType.INTEGER);
      s.addTypeDefinition(t1);
    }catch(MultipleDefinitionException mde){
      fail("exception thrown too early: " + mde.getMessage());
    }
    TypeDefinition t2 = new TypeDefinition("foo", PrimitiveType.STRING);
    s.addTypeDefinition(t2);
  }
  
  @Test(expected = MultipleDefinitionException.class)
  public void testAddTypeDef_maskDefaultType() throws MultipleDefinitionException{
    // Suppose we create a new Schematic and then try to redefine the meaning of "Int".
    // Since "Int" is a built-in type, this should result in a MultipleDefinitionException being thrown.
    Schematic s = new Schematic("test");
    TypeDefinition td = new TypeDefinition("Int", PrimitiveType.STRING);
    s.addTypeDefinition(td);
  }
  
  @Test
  public void testAddEndpointDef() throws MultipleDefinitionException {
    Schematic s = new Schematic("test");
    EndpointDefinition e1 = new EndpointDefinition("n1");
    s.addEndpointDefinition(e1);
  }
  
  @Test(expected=MultipleDefinitionException.class)
  public void testAddEndpointDef_multipleDefinitions() throws MultipleDefinitionException {
    // We should not be able to add two endpoint definitions whose first argument is the same string.
    Schematic s = new Schematic("test");
    try{
      EndpointDefinition n1 = new EndpointDefinition("foo");
      s.addEndpointDefinition(n1);
    }catch(MultipleDefinitionException mde){
      fail("exception thrown too early: " + mde.getMessage());
    }
    EndpointDefinition n2 = new EndpointDefinition("foo");
    s.addEndpointDefinition(n2);
  }
  
  @Test
  public void testAddNodeDef() throws MultipleDefinitionException {
    Schematic s = new Schematic("test");
    NodeDefinition n1 = new NodeDefinition("n1");
    s.addNodeDefinition(n1);
  }
  
  @Test(expected=MultipleDefinitionException.class)
  public void testAddNodeDef_multipleDefinitions() throws MultipleDefinitionException {
    // We should not be able to add two node definitions whose first argument is the same string.
    Schematic s = new Schematic("test");
    try{
      NodeDefinition n1 = new NodeDefinition("foo");
      s.addNodeDefinition(n1);
    }catch(MultipleDefinitionException mde){
      fail("exception thrown too early: " + mde.getMessage());
    }
    NodeDefinition n2 = new NodeDefinition("foo");
    s.addNodeDefinition(n2);
  }
  
  @Test
  public void testAddConnectionDef() throws MultipleDefinitionException {
    Schematic s = new Schematic("test");
    ConnectionDefinition c1 = new ConnectionDefinition("c1");
    s.addConnectionDefinition(c1);
  }
  
  @Test(expected=MultipleDefinitionException.class)
  public void testAddConnectionDef_multipleDefinitions() throws MultipleDefinitionException {
    // We should not be able to add two connection definitions whose first argument is the same string.
    Schematic s = new Schematic("test");
    try{
      ConnectionDefinition c1 = new ConnectionDefinition("foo");
      s.addConnectionDefinition(c1);
    }catch(MultipleDefinitionException mde){
      fail("exception thrown too early: " + mde.getMessage());
    }
    ConnectionDefinition c2 = new ConnectionDefinition("foo");
    s.addConnectionDefinition(c2);
  }
  
  @Test
  public void testAddConstraintDef() throws MultipleDefinitionException {
    Schematic s = new Schematic("test");
    ConstraintDefinition e1 = new ConstraintDefinition("c1", new BooleanLiteral(true));
    s.addConstraintDefinition(e1);
  }
  
  @Test(expected=MultipleDefinitionException.class)
  public void testAddConstraintDef_multipleDefinitions() throws MultipleDefinitionException {
    // We should not be able to add two constraint definitions whose first argument is the same string.
    Schematic s = new Schematic("test");
    try{
      ConstraintDefinition e1 = new ConstraintDefinition("foo", new BooleanLiteral(true));
      s.addConstraintDefinition(e1);
    }catch(MultipleDefinitionException mde){
      fail("exception thrown too early: " + mde.getMessage());
    }
    ConstraintDefinition e2 = new ConstraintDefinition("foo", new BooleanLiteral(false));
    s.addConstraintDefinition(e2);
  }

}
