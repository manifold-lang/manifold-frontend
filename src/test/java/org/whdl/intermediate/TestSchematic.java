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
  public void testAddPortDef() throws MultipleDefinitionException {
    Schematic s = new Schematic("test");
    PortType e1 = new PortType(new HashMap<String, Type>());
    s.addPortTypeDefinition("n1",e1);
  }
  
  @Test(expected=MultipleDefinitionException.class)
  public void testAddPortDef_multipleDefinitions() throws MultipleDefinitionException {
    // We should not be able to add two port definitions whose first argument is the same string.
    Schematic s = new Schematic("test");
    try{
      PortType n1 = new PortType(new HashMap<String, Type>());
      s.addPortTypeDefinition("foo", n1);
    }catch(MultipleDefinitionException mde){
      fail("exception thrown too early: " + mde.getMessage());
    }
    PortType n2 = new PortType(new HashMap<String, Type>());
    s.addPortTypeDefinition("foo", n2);
  }
  
  @Test
  public void testGetPortDef() throws UndeclaredIdentifierException, MultipleDefinitionException{
    Schematic s = new Schematic("test");
    PortType expected = new PortType(new HashMap<String, Type>());
    s.addPortTypeDefinition("foo", expected);
    PortType actual = s.getPortTypeDefinition("foo");
    assertEquals(expected, actual);
  }
  
  @Test(expected=UndeclaredIdentifierException.class)
  public void testGetPortDef_notDeclared() throws UndeclaredIdentifierException {
    Schematic s = new Schematic("test");
    PortType bogus = s.getPortTypeDefinition("does-not-exist");
  }
  
  @Test
  public void testAddNodeDef() throws MultipleDefinitionException {
    Schematic s = new Schematic("test");
    NodeType n1 = new NodeType(new HashMap<String, Type>(), new HashMap<String, PortType>());
    s.addNodeTypeDefinition("n1", n1);
  }
  
  @Test(expected=MultipleDefinitionException.class)
  public void testAddNodeDef_multipleDefinitions() throws MultipleDefinitionException {
    // We should not be able to add two node definitions whose first argument is the same string.
    Schematic s = new Schematic("test");
    try{
      NodeType n1 = new NodeType(new HashMap<String, Type>(), new HashMap<String, PortType>());
      s.addNodeTypeDefinition("foo", n1);
    }catch(MultipleDefinitionException mde){
      fail("exception thrown too early: " + mde.getMessage());
    }
    NodeType n2 = new NodeType(new HashMap<String, Type>(), new HashMap<String, PortType>());
    s.addNodeTypeDefinition("foo", n2);
  }
  
  @Test
  public void testGetNodeDef() throws UndeclaredIdentifierException, MultipleDefinitionException{
    Schematic s = new Schematic("test");
    NodeType expected = new NodeType(new HashMap<String, Type>(), new HashMap<String, PortType>());
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
    // ConnectionDefinition, NodeDefinition, and PortDefinition with the same name
    // without encountering a "multiple definition" exception.
    Schematic s = new Schematic("test");
    
    Type t1 = StringType.getInstance();
    ConstraintType ct1 = new ConstraintType(new HashMap<String, Type>());
    ConnectionType cn1 = new ConnectionType(new HashMap<String, Type>());
    NodeType n1 = new NodeType(new HashMap<String, Type>(), new HashMap<String, PortType>());
    PortType e1 = new PortType(new HashMap<String, Type>());
    
    s.addUserDefinedTypeDefinition("foo", t1);
    s.addConstraintTypeDefinition("foo", ct1);
    s.addConnectionTypeDefinition("foo", cn1);
    s.addNodeTypeDefinition("foo", n1);
    s.addPortTypeDefinition("foo", e1);
  }
  
  @Test
  public void testAddNode() throws UndeclaredIdentifierException, MultipleInstantiationException{
    Schematic s = new Schematic("test");
    NodeType n1_type = new NodeType(new HashMap<String, Type>(), new HashMap<String, PortType>());
    Node n1 = new Node(n1_type);
    s.addNode("n1", n1);
  }
  
  @Test(expected=org.whdl.intermediate.MultipleInstantiationException.class)
  public void testAddNode_multipleInstantiation() throws MultipleInstantiationException{
    Schematic s = new Schematic("test");
    NodeType n1_type = new NodeType(new HashMap<String, Type>(), new HashMap<String, PortType>());
    try{
      Node n1 = new Node(n1_type);
      s.addNode("n1", n1);
    }catch(MultipleInstantiationException mie){
      fail("exception thrown too early");
    }
    Node n1_dup = new Node(n1_type);
    s.addNode("n1", n1_dup);
  }
  
  @Test
  public void testGetNode() throws MultipleInstantiationException, UndeclaredIdentifierException{
    Schematic s = new Schematic("test");
    NodeType n1_type = new NodeType(new HashMap<String, Type>(), new HashMap<String, PortType>());
    Node n1 = new Node(n1_type);
    s.addNode("n1", n1);
    
    Node actual = s.getNode("n1");
    assertSame(n1, actual);
  }
  
  @Test(expected=org.whdl.intermediate.UndeclaredIdentifierException.class)
  public void testGetNode_notInstantiated() throws UndeclaredIdentifierException{
    Schematic s = new Schematic("test");
    s.getNode("bogus");
  }
  
  @Test
  public void testAddConnection() throws MultipleInstantiationException{
    Schematic s = new Schematic("test");
    ConnectionType c1_type = new ConnectionType(new HashMap<String,Type>());
    Connection c1 = new Connection(c1_type);
    s.addConnection("c1", c1);
  }
  
  @Test(expected=org.whdl.intermediate.MultipleInstantiationException.class)
  public void testAddConnection_multipleInstantiation() throws MultipleInstantiationException{
    Schematic s = new Schematic("test");
    ConnectionType c1_type = new ConnectionType(new HashMap<String,Type>());
    try{
      Connection c1 = new Connection(c1_type);
      s.addConnection("c1", c1);
    }catch(MultipleInstantiationException mie){
      fail("exception thrown too early");
    }
    Connection c1_dup = new Connection(c1_type);
    s.addConnection("c1", c1_dup);
  }
  
  @Test
  public void testGetConnection() throws MultipleInstantiationException, UndeclaredIdentifierException{
    Schematic s = new Schematic("test");
    ConnectionType c1_type = new ConnectionType(new HashMap<String,Type>());
    Connection c1 = new Connection(c1_type);
    s.addConnection("c1", c1);
    Connection actual = s.getConnection("c1");
    assertSame(c1, actual);
  }
  
  @Test(expected=org.whdl.intermediate.UndeclaredIdentifierException.class)
  public void testGetConnection_notInstantiated() throws UndeclaredIdentifierException{
    Schematic s = new Schematic("test");
    s.getConnection("bogus");
  }
  
  @Test
  public void testAddConstraint() throws MultipleInstantiationException{
    Schematic s = new Schematic("test");
    ConstraintType c1_type = new ConstraintType(new HashMap<String,Type>());
    Constraint c1 = new Constraint(c1_type);
    s.addConstraint("c1", c1);
  }
  
  @Test(expected=org.whdl.intermediate.MultipleInstantiationException.class)
  public void testAddConstraint_multipleInstantiation() throws MultipleInstantiationException{
    Schematic s = new Schematic("test");
    ConstraintType c1_type = new ConstraintType(new HashMap<String,Type>());
    try{
      Constraint c1 = new Constraint(c1_type);
      s.addConstraint("c1", c1);
    }catch(MultipleInstantiationException mie){
      fail("exception thrown too early");
    }
    Constraint c1_dup = new Constraint(c1_type);
    s.addConstraint("c1", c1_dup);
  }
  
  @Test
  public void testGetConstraint() throws MultipleInstantiationException, UndeclaredIdentifierException{
    Schematic s = new Schematic("test");
    ConstraintType c1_type = new ConstraintType(new HashMap<String,Type>());
    Constraint c1 = new Constraint(c1_type);
    s.addConstraint("c1", c1);
    
    Constraint actual = s.getConstraint("c1");
    assertSame(c1, actual);
  }
  
  @Test(expected=org.whdl.intermediate.UndeclaredIdentifierException.class)
  public void testGetConstraint_notInstantiated() throws UndeclaredIdentifierException{
    Schematic s = new Schematic("test");
    s.getConstraint("bogus");
  }
  
}
