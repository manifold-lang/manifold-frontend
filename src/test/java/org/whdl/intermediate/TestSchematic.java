package org.whdl.intermediate;

import static org.junit.Assert.*;

import org.junit.Test;
import org.whdl.intermediate.definitions.*;
import org.whdl.intermediate.exceptions.MultipleDefinitionException;
import org.whdl.intermediate.types.PrimitiveType;

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
    EndpointDefinition e1 = new EndpointDefinition("e1");
    s.addEndpointDefinition(e1);
  }
  
  @Test(expected=MultipleDefinitionException.class)
  public void testAddEndpointDef_multipleDefinitions() throws MultipleDefinitionException {
    // We should not be able to add two endpoint definitions whose first argument is the same string.
    Schematic s = new Schematic("test");
    try{
      EndpointDefinition e1 = new EndpointDefinition("foo");
      s.addEndpointDefinition(e1);
    }catch(MultipleDefinitionException mde){
      fail("exception thrown too early: " + mde.getMessage());
    }
    EndpointDefinition e2 = new EndpointDefinition("foo");
    s.addEndpointDefinition(e2);
  }

}
