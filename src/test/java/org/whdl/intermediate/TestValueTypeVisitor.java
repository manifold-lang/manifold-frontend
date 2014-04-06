package org.whdl.intermediate;

import static org.junit.Assert.*;

import org.junit.Test;
import org.whdl.intermediate.definitions.ConnectionDefinition;
import org.whdl.intermediate.definitions.ConstraintDefinition;
import org.whdl.intermediate.definitions.EndpointDefinition;
import org.whdl.intermediate.definitions.NodeDefinition;
import org.whdl.intermediate.types.*;

public class TestValueTypeVisitor {

  @Test
  public void testVisitBooleanLiteral() {
    Value e = new BooleanValue(true);
    ValueTypeVisitor v = new ValueTypeVisitor();
    Type expected = PrimitiveType.BOOLEAN;
    Type actual = e.acceptVisitor(v);
    assertEquals(expected, actual);
  }

  @Test
  public void testVisitIntegerLiteral() {
    Value e = new IntegerValue(1);
    ValueTypeVisitor v = new ValueTypeVisitor();
    Type expected = PrimitiveType.INTEGER;
    Type actual = e.acceptVisitor(v);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testVisitStringLiteral() {
    Value e = new StringValue("foo");
    ValueTypeVisitor v = new ValueTypeVisitor();
    Type expected = PrimitiveType.STRING;
    Type actual = e.acceptVisitor(v);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testVisitConnection(){
    ValueTypeVisitor v = new ValueTypeVisitor();
    
    NodeDefinition nDef = new NodeDefinition("nod");
    Node n1 = new Node("nod-1", nDef);
    
    EndpointDefinition eDef = new EndpointDefinition("ept");
    Endpoint e1 = new Endpoint("ept-1", eDef, n1);
    Endpoint e2 = new Endpoint("ept-2", eDef, n1);
    
    ConnectionDefinition cDef = new ConnectionDefinition("foo");
    Value e = new Connection("foo-1", cDef, e1, e2);
    
    Type expected = new ConnectionType("foo");
    Type actual = e.acceptVisitor(v);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testVisitConstraint(){
    ValueTypeVisitor v = new ValueTypeVisitor();
    
    ConstraintDefinition cDef = new ConstraintDefinition("con");
    Value e = new Constraint("con-1", cDef);
    
    Type expected = new ConstraintType("con");
    Type actual = e.acceptVisitor(v);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testVisitEndpoint(){
    ValueTypeVisitor v = new ValueTypeVisitor();
    
    NodeDefinition nDef = new NodeDefinition("nod");
    Node n1 = new Node("nod-1", nDef);
    
    EndpointDefinition eDef = new EndpointDefinition("ept");
    Value e = new Endpoint("ept-1", eDef, n1);
    
    Type expected = new EndpointType("ept");
    Type actual = e.acceptVisitor(v);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testVisitNode(){
    ValueTypeVisitor v = new ValueTypeVisitor();
    
    NodeDefinition nDef = new NodeDefinition("nod");
    Value e = new Node("nod-1", nDef);
    
    Type expected = new NodeType("nod");
    Type actual = e.acceptVisitor(v);
    assertEquals(expected, actual);
  }
  
}
