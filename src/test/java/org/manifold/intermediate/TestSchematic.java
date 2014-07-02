package org.manifold.intermediate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

public class TestSchematic {
  
  private static final PortType defaultPortDefinition = new PortType(new HashMap<String, Type>());
  private static final String PORT_NAME1 = "P1";
  private static final String PORT_NAME2 = "P2";
  
  private Node n;
  private Port p1, p2;
  
  @Before
  public void setup() throws UndeclaredIdentifierException {
    HashMap<String, PortType> portMap = new HashMap<>();
    portMap.put(PORT_NAME1, defaultPortDefinition);
    portMap.put(PORT_NAME2, defaultPortDefinition);
    n = new Node(new NodeType(new HashMap<String, Type>(), portMap));
    
    p1 = n.getPort(PORT_NAME1);
    p2 = n.getPort(PORT_NAME2);
  }

  @Test
  public void testAddTypeDef() throws MultipleDefinitionException {
    Schematic s = new Schematic("test");
    Type t1 = IntegerType.getInstance();
    s.addUserDefinedType("foo", t1);
  }
  
  @Test(expected = MultipleDefinitionException.class)
  public void testAddTypeDef_multipleDefinitions() throws MultipleDefinitionException{
    // We should not be able to add two type definitions whose first argument is the same string.
    Schematic s = new Schematic("test");
    try{
      Type t1 = IntegerType.getInstance();
      s.addUserDefinedType("foo", t1);
    }catch(MultipleDefinitionException mde){
      fail("exception thrown too early: " + mde.getMessage());
    }
    Type t2 = IntegerType.getInstance();
    s.addUserDefinedType("foo", t2);
  }
  
  @Test(expected = MultipleDefinitionException.class)
  public void testAddTypeDef_maskDefaultType() throws MultipleDefinitionException{
    // Suppose we create a new Schematic and then try to redefine the meaning of "Int".
    // Since "Int" is a built-in type, this should result in a MultipleDefinitionException being thrown.
    Schematic s = new Schematic("test");
    Type td = StringType.getInstance();
    s.addUserDefinedType("Int", td);
  }
  
  @Test
  public void testGetTypeDef() throws UndeclaredIdentifierException, MultipleDefinitionException{
    Schematic s = new Schematic("test");
    Type expected = IntegerType.getInstance();
    s.addUserDefinedType("foo", expected);
    Type actual = s.getUserDefinedType("foo");
    assertEquals(expected, actual);
  }
  
  @Test(expected=UndeclaredIdentifierException.class)
  public void testGetTypeDef_notDeclared() throws UndeclaredIdentifierException {
    Schematic s = new Schematic("test");
    Type bogus = s.getUserDefinedType("does-not-exist");
  }
  
  @Test
  public void testAddPortDef() throws MultipleDefinitionException {
    Schematic s = new Schematic("test");
    PortType e1 = new PortType(new HashMap<String, Type>());
    s.addPortType("n1",e1);
  }
  
  @Test(expected=MultipleDefinitionException.class)
  public void testAddPortDef_multipleDefinitions() throws MultipleDefinitionException {
    // We should not be able to add two port definitions whose first argument is the same string.
    Schematic s = new Schematic("test");
    try{
      PortType n1 = new PortType(new HashMap<String, Type>());
      s.addPortType("foo", n1);
    }catch(MultipleDefinitionException mde){
      fail("exception thrown too early: " + mde.getMessage());
    }
    PortType n2 = new PortType(new HashMap<String, Type>());
    s.addPortType("foo", n2);
  }
  
  @Test
  public void testGetPortDef() throws UndeclaredIdentifierException, MultipleDefinitionException{
    Schematic s = new Schematic("test");
    PortType expected = new PortType(new HashMap<String, Type>());
    s.addPortType("foo", expected);
    PortType actual = s.getPortType("foo");
    assertEquals(expected, actual);
  }
  
  @Test(expected=UndeclaredIdentifierException.class)
  public void testGetPortDef_notDeclared() throws UndeclaredIdentifierException {
    Schematic s = new Schematic("test");
    PortType bogus = s.getPortType("does-not-exist");
  }
  
  @Test
  public void testAddNodeDef() throws MultipleDefinitionException {
    Schematic s = new Schematic("test");
    NodeType n1 = new NodeType(new HashMap<String, Type>(), new HashMap<String, PortType>());
    s.addNodeType("n1", n1);
  }
  
  @Test(expected=MultipleDefinitionException.class)
  public void testAddNodeDef_multipleDefinitions() throws MultipleDefinitionException {
    // We should not be able to add two node definitions whose first argument is the same string.
    Schematic s = new Schematic("test");
    try{
      NodeType n1 = new NodeType(new HashMap<String, Type>(), new HashMap<String, PortType>());
      s.addNodeType("foo", n1);
    }catch(MultipleDefinitionException mde){
      fail("exception thrown too early: " + mde.getMessage());
    }
    NodeType n2 = new NodeType(new HashMap<String, Type>(), new HashMap<String, PortType>());
    s.addNodeType("foo", n2);
  }
  
  @Test
  public void testGetNodeDef() throws UndeclaredIdentifierException, MultipleDefinitionException{
    Schematic s = new Schematic("test");
    NodeType expected = new NodeType(new HashMap<String, Type>(), new HashMap<String, PortType>());
    s.addNodeType("foo", expected);
    NodeType actual = s.getNodeType("foo");
    assertEquals(expected, actual);
  }
  
  @Test(expected=UndeclaredIdentifierException.class)
  public void testGetNodeDef_notDeclared() throws UndeclaredIdentifierException {
    Schematic s = new Schematic("test");
    NodeType bogus = s.getNodeType("does-not-exist");
  }
  
  @Test
  public void testAddConnectionDef() throws MultipleDefinitionException {
    Schematic s = new Schematic("test");
    ConnectionType c1 = new ConnectionType(new HashMap<String, Type>());
    s.addConnectionType("c1", c1);
  }
  
  @Test(expected=MultipleDefinitionException.class)
  public void testAddConnectionDef_multipleDefinitions() throws MultipleDefinitionException {
    // We should not be able to add two connection definitions whose first argument is the same string.
    Schematic s = new Schematic("test");
    try{
      ConnectionType c1 = new ConnectionType(new HashMap<String, Type>());
      s.addConnectionType("foo", c1);
    }catch(MultipleDefinitionException mde){
      fail("exception thrown too early: " + mde.getMessage());
    }
    ConnectionType c2 = new ConnectionType(new HashMap<String, Type>());
    s.addConnectionType("foo", c2);
  }
  
  @Test
  public void testGetConnectionDef() throws UndeclaredIdentifierException, MultipleDefinitionException{
    Schematic s = new Schematic("test");
    ConnectionType expected = new ConnectionType(new HashMap<String, Type>());
    s.addConnectionType("foo", expected);
    ConnectionType actual = s.getConnectionType("foo");
    assertEquals(expected, actual);
  }
  
  @Test(expected=UndeclaredIdentifierException.class)
  public void testGetConnectionDef_notDeclared() throws UndeclaredIdentifierException {
    Schematic s = new Schematic("test");
    ConnectionType bogus = s.getConnectionType("does-not-exist");
  }
  
  @Test
  public void testAddConstraintDef() throws MultipleDefinitionException {
    Schematic s = new Schematic("test");
    ConstraintType e1 = new ConstraintType(new HashMap<String, Type>());
    s.addConstraintType("e1", e1);
  }
  
  @Test(expected=MultipleDefinitionException.class)
  public void testAddConstraintDef_multipleDefinitions() throws MultipleDefinitionException {
    // We should not be able to add two constraint definitions whose first argument is the same string.
    Schematic s = new Schematic("test");
    try{
      ConstraintType e1 = new ConstraintType(new HashMap<String, Type>());
      s.addConstraintType("foo", e1);
    }catch(MultipleDefinitionException mde){
      fail("exception thrown too early: " + mde.getMessage());
    }
    ConstraintType e2 = new ConstraintType(new HashMap<String, Type>());
    s.addConstraintType("foo", e2);
  }

  @Test
  public void testGetConstraintDef() throws UndeclaredIdentifierException, MultipleDefinitionException{
    Schematic s = new Schematic("test");
    ConstraintType expected = new ConstraintType(new HashMap<String, Type>());
    s.addConstraintType("foo", expected);
    ConstraintType actual = s.getConstraintType("foo");
    assertEquals(expected, actual);
  }
  
  @Test(expected=UndeclaredIdentifierException.class)
  public void testGetConstraintDef_notDeclared() throws UndeclaredIdentifierException {
    Schematic s = new Schematic("test");
    ConstraintType bogus = s.getConstraintType("does-not-exist");
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
    
    s.addUserDefinedType("foo", t1);
    s.addConstraintType("foo", ct1);
    s.addConnectionType("foo", cn1);
    s.addNodeType("foo", n1);
    s.addPortType("foo", e1);
  }
  
  @Test
  public void testAddNode() throws UndeclaredIdentifierException, MultipleAssignmentException{
    Schematic s = new Schematic("test");
    NodeType n1_type = new NodeType(new HashMap<String, Type>(), new HashMap<String, PortType>());
    Node n1 = new Node(n1_type);
    s.addNode("n1", n1);
  }
  
  @Test(expected=org.manifold.intermediate.MultipleAssignmentException.class)
  public void testAddNode_multipleInstantiation() throws MultipleAssignmentException{
    Schematic s = new Schematic("test");
    NodeType n1_type = new NodeType(new HashMap<String, Type>(), new HashMap<String, PortType>());
    try{
      Node n1 = new Node(n1_type);
      s.addNode("n1", n1);
    }catch(MultipleAssignmentException mie){
      fail("exception thrown too early");
    }
    Node n1_dup = new Node(n1_type);
    s.addNode("n1", n1_dup);
  }
  
  @Test
  public void testGetNode() throws MultipleAssignmentException, UndeclaredIdentifierException{
    Schematic s = new Schematic("test");
    NodeType n1_type = new NodeType(new HashMap<String, Type>(), new HashMap<String, PortType>());
    Node n1 = new Node(n1_type);
    s.addNode("n1", n1);
    
    Node actual = s.getNode("n1");
    assertSame(n1, actual);
  }
  
  @Test(expected=org.manifold.intermediate.UndeclaredIdentifierException.class)
  public void testGetNode_notInstantiated() throws UndeclaredIdentifierException{
    Schematic s = new Schematic("test");
    s.getNode("bogus");
  }
  
  @Test
  public void testAddConnection() throws MultipleAssignmentException{
    Schematic s = new Schematic("test");
    ConnectionType c1_type = new ConnectionType(new HashMap<String,Type>());
    Connection c1 = new Connection(c1_type, p1, p2);
    s.addConnection("c1", c1);
  }
  
  @Test(expected=org.manifold.intermediate.MultipleAssignmentException.class)
  public void testAddConnection_multipleInstantiation() throws MultipleAssignmentException{
    Schematic s = new Schematic("test");
    ConnectionType c1_type = new ConnectionType(new HashMap<String,Type>());
    try{
      Connection c1 = new Connection(c1_type, p1, p2);
      s.addConnection("c1", c1);
    }catch(MultipleAssignmentException mie){
      fail("exception thrown too early");
    }
    Connection c1_dup = new Connection(c1_type, p1, p2);
    s.addConnection("c1", c1_dup);
  }
  
  @Test
  public void testGetConnection() throws MultipleAssignmentException, UndeclaredIdentifierException{
    Schematic s = new Schematic("test");
    ConnectionType c1_type = new ConnectionType(new HashMap<String,Type>());
    Connection c1 = new Connection(c1_type, p1, p2);
    s.addConnection("c1", c1);
    Connection actual = s.getConnection("c1");
    assertSame(c1, actual);
  }
  
  @Test(expected=org.manifold.intermediate.UndeclaredIdentifierException.class)
  public void testGetConnection_notInstantiated() throws UndeclaredIdentifierException{
    Schematic s = new Schematic("test");
    s.getConnection("bogus");
  }
  
  @Test
  public void testAddConstraint() throws MultipleAssignmentException{
    Schematic s = new Schematic("test");
    ConstraintType c1_type = new ConstraintType(new HashMap<String,Type>());
    Constraint c1 = new Constraint(c1_type);
    s.addConstraint("c1", c1);
  }
  
  @Test(expected=org.manifold.intermediate.MultipleAssignmentException.class)
  public void testAddConstraint_multipleInstantiation() throws MultipleAssignmentException{
    Schematic s = new Schematic("test");
    ConstraintType c1_type = new ConstraintType(new HashMap<String,Type>());
    try{
      Constraint c1 = new Constraint(c1_type);
      s.addConstraint("c1", c1);
    }catch(MultipleAssignmentException mie){
      fail("exception thrown too early");
    }
    Constraint c1_dup = new Constraint(c1_type);
    s.addConstraint("c1", c1_dup);
  }
  
  @Test
  public void testGetConstraint() throws MultipleAssignmentException, UndeclaredIdentifierException{
    Schematic s = new Schematic("test");
    ConstraintType c1_type = new ConstraintType(new HashMap<String,Type>());
    Constraint c1 = new Constraint(c1_type);
    s.addConstraint("c1", c1);
    
    Constraint actual = s.getConstraint("c1");
    assertSame(c1, actual);
  }
  
  @Test(expected=org.manifold.intermediate.UndeclaredIdentifierException.class)
  public void testGetConstraint_notInstantiated() throws UndeclaredIdentifierException{
    Schematic s = new Schematic("test");
    s.getConstraint("bogus");
  }
  
}
