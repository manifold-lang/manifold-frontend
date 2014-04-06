package org.whdl.intermediate;

import static org.junit.Assert.*;

import org.junit.Test;
import org.whdl.intermediate.definitions.ConnectionDefinition;
import org.whdl.intermediate.definitions.ConstraintDefinition;
import org.whdl.intermediate.definitions.EndpointDefinition;
import org.whdl.intermediate.definitions.NodeDefinition;
import org.whdl.intermediate.types.*;

public class TestDomainObjectTypeVisitor {

  @Test
  public void testVisitBooleanLiteral() {
    DomainObject e = new BooleanLiteral(true);
    DomainObjectTypeVisitor v = new DomainObjectTypeVisitor();
    Type expected = PrimitiveType.BOOLEAN;
    Type actual = e.acceptVisitor(v);
    assertEquals(expected, actual);
  }

  @Test
  public void testVisitIntegerLiteral() {
    DomainObject e = new IntegerLiteral(1);
    DomainObjectTypeVisitor v = new DomainObjectTypeVisitor();
    Type expected = PrimitiveType.INTEGER;
    Type actual = e.acceptVisitor(v);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testVisitStringLiteral() {
    DomainObject e = new StringLiteral("foo");
    DomainObjectTypeVisitor v = new DomainObjectTypeVisitor();
    Type expected = PrimitiveType.STRING;
    Type actual = e.acceptVisitor(v);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testVisitConnection(){
    DomainObjectTypeVisitor v = new DomainObjectTypeVisitor();
    
    NodeDefinition nDef = new NodeDefinition("nod");
    Node n1 = new Node("nod-1", nDef);
    
    EndpointDefinition eDef = new EndpointDefinition("ept");
    Endpoint e1 = new Endpoint("ept-1", eDef, n1);
    Endpoint e2 = new Endpoint("ept-2", eDef, n1);
    
    ConnectionDefinition cDef = new ConnectionDefinition("foo");
    DomainObject e = new Connection("foo-1", cDef, e1, e2);
    
    Type expected = new ConnectionType("foo");
    Type actual = e.acceptVisitor(v);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testVisitConstraint(){
    DomainObjectTypeVisitor v = new DomainObjectTypeVisitor();
    
    ConstraintDefinition cDef = new ConstraintDefinition("con");
    DomainObject e = new Constraint("con-1", cDef);
    
    Type expected = new ConstraintType("con");
    Type actual = e.acceptVisitor(v);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testVisitEndpoint(){
    DomainObjectTypeVisitor v = new DomainObjectTypeVisitor();
    
    NodeDefinition nDef = new NodeDefinition("nod");
    Node n1 = new Node("nod-1", nDef);
    
    EndpointDefinition eDef = new EndpointDefinition("ept");
    DomainObject e = new Endpoint("ept-1", eDef, n1);
    
    Type expected = new EndpointType("ept");
    Type actual = e.acceptVisitor(v);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testVisitNode(){
    DomainObjectTypeVisitor v = new DomainObjectTypeVisitor();
    
    NodeDefinition nDef = new NodeDefinition("nod");
    DomainObject e = new Node("nod-1", nDef);
    
    Type expected = new NodeType("nod");
    Type actual = e.acceptVisitor(v);
    assertEquals(expected, actual);
  }
  
}
