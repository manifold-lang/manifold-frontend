package org.whdl.intermediate;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

public class TestSchematic {

  @Test
  public void testAddTypeDef() throws MultipleDefinitionException {
    Schematic s = new Schematic("test");
    Type t1 = IntegerType.getInstance();
    s.addUserDefinedTypeDefinition("foo", t1);
  }
  
  @Test(expected = MultipleDefinitionException.class)
  public void testAddTypeDef_multipleDefinitions() throws MultipleDefinitionException{
    // We should not be able to add two type definitions whose first argument is the same string.
    Schematic s = new Schematic("test");
    try{
      Type t1 = IntegerType.getInstance();
      s.addUserDefinedTypeDefinition("foo", t1);
    }catch(MultipleDefinitionException mde){
      fail("exception thrown too early: " + mde.getMessage());
    }
    Type t2 = IntegerType.getInstance();
    s.addUserDefinedTypeDefinition("foo", t2);
  }
  
  @Test(expected = MultipleDefinitionException.class)
  public void testAddTypeDef_maskDefaultType() throws MultipleDefinitionException{
    // Suppose we create a new Schematic and then try to redefine the meaning of "Int".
    // Since "Int" is a built-in type, this should result in a MultipleDefinitionException being thrown.
    Schematic s = new Schematic("test");
    Type td = StringType.getInstance();
    s.addUserDefinedTypeDefinition("Int", td);
  }
  
  @Test
  public void testGetTypeDef() throws UndeclaredIdentifierException, MultipleDefinitionException{
    Schematic s = new Schematic("test");
    Type expected = IntegerType.getInstance();
    s.addUserDefinedTypeDefinition("foo", expected);
    Type actual = s.getUserDefinedTypeDefinition("foo");
    assertEquals(expected, actual);
  }
  
  @Test(expected=UndeclaredIdentifierException.class)
  public void testGetTypeDef_notDeclared() throws UndeclaredIdentifierException {
    Schematic s = new Schematic("test");
    Type bogus = s.getUserDefinedTypeDefinition("does-not-exist");
  }
  
  @Test
  public void testAddEndpointDef() throws MultipleDefinitionException {
    Schematic s = new Schematic("test");
    EndpointType e1 = new EndpointType(new HashMap<String, Type>());
    s.addEndpointTypeDefinition("n1",e1);
  }
  
  @Test(expected=MultipleDefinitionException.class)
  public void testAddEndpointDef_multipleDefinitions() throws MultipleDefinitionException {
    // We should not be able to add two endpoint definitions whose first argument is the same string.
    Schematic s = new Schematic("test");
    try{
      EndpointType n1 = new EndpointType(new HashMap<String, Type>());
      s.addEndpointTypeDefinition("foo", n1);
    }catch(MultipleDefinitionException mde){
      fail("exception thrown too early: " + mde.getMessage());
    }
    EndpointType n2 = new EndpointType(new HashMap<String, Type>());
    s.addEndpointTypeDefinition("foo", n2);
  }
  
  @Test
  public void testGetEndpointDef() throws UndeclaredIdentifierException, MultipleDefinitionException{
    Schematic s = new Schematic("test");
    EndpointType expected = new EndpointType(new HashMap<String, Type>());
    s.addEndpointTypeDefinition("foo", expected);
    EndpointType actual = s.getEndpointTypeDefinition("foo");
    assertEquals(expected, actual);
  }
  
  @Test(expected=UndeclaredIdentifierException.class)
  public void testGetEndpointDef_notDeclared() throws UndeclaredIdentifierException {
    Schematic s = new Schematic("test");
    EndpointType bogus = s.getEndpointTypeDefinition("does-not-exist");
  }
  
  @Test
  public void testAddNodeDef() throws MultipleDefinitionException {
    Schematic s = new Schematic("test");
    NodeType n1 = new NodeType(new HashMap<String, Type>(), new HashMap<String, EndpointType>());
    s.addNodeTypeDefinition("n1", n1);
  }
  
  @Test(expected=MultipleDefinitionException.class)
  public void testAddNodeDef_multipleDefinitions() throws MultipleDefinitionException {
    // We should not be able to add two node definitions whose first argument is the same string.
    Schematic s = new Schematic("test");
    try{
      NodeType n1 = new NodeType(new HashMap<String, Type>(), new HashMap<String, EndpointType>());
      s.addNodeTypeDefinition("foo", n1);
    }catch(MultipleDefinitionException mde){
      fail("exception thrown too early: " + mde.getMessage());
    }
    NodeType n2 = new NodeType(new HashMap<String, Type>(), new HashMap<String, EndpointType>());
    s.addNodeTypeDefinition("foo", n2);
  }
  
  @Test
  public void testGetNodeDef() throws UndeclaredIdentifierException, MultipleDefinitionException{
    Schematic s = new Schematic("test");
    NodeType expected = new NodeType(new HashMap<String, Type>(), new HashMap<String, EndpointType>());
    s.addNodeTypeDefinition("foo", expected);
    NodeType actual = s.getNodeTypeDefinition("foo");
    assertEquals(expected, actual);
  }
  
  @Test(expected=UndeclaredIdentifierException.class)
  public void testGetNodeDef_notDeclared() throws UndeclaredIdentifierException {
    Schematic s = new Schematic("test");
    NodeType bogus = s.getNodeTypeDefinition("does-not-exist");
  }
  
  @Test
  public void testAddConnectionDef() throws MultipleDefinitionException {
    Schematic s = new Schematic("test");
    ConnectionType c1 = new ConnectionType(new HashMap<String, Type>());
    s.addConnectionTypeDefinition("c1", c1);
  }
  
  @Test(expected=MultipleDefinitionException.class)
  public void testAddConnectionDef_multipleDefinitions() throws MultipleDefinitionException {
    // We should not be able to add two connection definitions whose first argument is the same string.
    Schematic s = new Schematic("test");
    try{
      ConnectionType c1 = new ConnectionType(new HashMap<String, Type>());
      s.addConnectionTypeDefinition("foo", c1);
    }catch(MultipleDefinitionException mde){
      fail("exception thrown too early: " + mde.getMessage());
    }
    ConnectionType c2 = new ConnectionType(new HashMap<String, Type>());
    s.addConnectionTypeDefinition("foo", c2);
  }
  
  @Test
  public void testGetConnectionDef() throws UndeclaredIdentifierException, MultipleDefinitionException{
    Schematic s = new Schematic("test");
    ConnectionType expected = new ConnectionType(new HashMap<String, Type>());
    s.addConnectionTypeDefinition("foo", expected);
    ConnectionType actual = s.getConnectionTypeDefinition("foo");
    assertEquals(expected, actual);
  }
  
  @Test(expected=UndeclaredIdentifierException.class)
  public void testGetConnectionDef_notDeclared() throws UndeclaredIdentifierException {
    Schematic s = new Schematic("test");
    ConnectionType bogus = s.getConnectionTypeDefinition("does-not-exist");
  }
  
  @Test
  public void testAddConstraintDef() throws MultipleDefinitionException {
    Schematic s = new Schematic("test");
    ConstraintType e1 = new ConstraintType(new HashMap<String, Type>());
    s.addConstraintTypeDefinition("e1", e1);
  }
  
  @Test(expected=MultipleDefinitionException.class)
  public void testAddConstraintDef_multipleDefinitions() throws MultipleDefinitionException {
    // We should not be able to add two constraint definitions whose first argument is the same string.
    Schematic s = new Schematic("test");
    try{
      ConstraintType e1 = new ConstraintType(new HashMap<String, Type>());
      s.addConstraintTypeDefinition("foo", e1);
    }catch(MultipleDefinitionException mde){
      fail("exception thrown too early: " + mde.getMessage());
    }
    ConstraintType e2 = new ConstraintType(new HashMap<String, Type>());
    s.addConstraintTypeDefinition("foo", e2);
  }

  @Test
  public void testGetConstraintDef() throws UndeclaredIdentifierException, MultipleDefinitionException{
    Schematic s = new Schematic("test");
    ConstraintType expected = new ConstraintType(new HashMap<String, Type>());
    s.addConstraintTypeDefinition("foo", expected);
    ConstraintType actual = s.getConstraintTypeDefinition("foo");
    assertEquals(expected, actual);
  }
  
  @Test(expected=UndeclaredIdentifierException.class)
  public void testGetConstraintDef_notDeclared() throws UndeclaredIdentifierException {
    Schematic s = new Schematic("test");
    ConstraintType bogus = s.getConstraintTypeDefinition("does-not-exist");
  }
  
  @Test
  public void testSeparationOfNamespaces_Definitions() throws MultipleDefinitionException{
    // We should be able to add one of each of a TypeDefinition, ConstraintDefinition,
    // ConnectionDefinition, NodeDefinition, and EndpointDefinition with the same name
    // without encountering a "multiple definition" exception.
    Schematic s = new Schematic("test");
    
    Type t1 = StringType.getInstance();
    ConstraintType ct1 = new ConstraintType(new HashMap<String, Type>());
    ConnectionType cn1 = new ConnectionType(new HashMap<String, Type>());
    NodeType n1 = new NodeType(new HashMap<String, Type>(), new HashMap<String, EndpointType>());
    EndpointType e1 = new EndpointType(new HashMap<String, Type>());
    
    s.addUserDefinedTypeDefinition("foo", t1);
    s.addConstraintTypeDefinition("foo", ct1);
    s.addConnectionTypeDefinition("foo", cn1);
    s.addNodeTypeDefinition("foo", n1);
    s.addEndpointTypeDefinition("foo", e1);
  }
  
}
