package org.whdl.intermediate;

import static org.junit.Assert.*;

import org.junit.Test;
import org.whdl.intermediate.types.*;

public class TestDomainObjectTypeVisitor {

  @Test
  public void testVisitBooleanLiteral() {
    DomainObject e = new BooleanLiteral(true);
    DomainObjectTypeVisitor v = new DomainObjectTypeVisitor();
    Type expected = PrimitiveType.BOOLEAN;
    Type actual = e.accept(v);
    assertEquals(expected, actual);
  }

  @Test
  public void testVisitIntegerLiteral() {
    DomainObject e = new IntegerLiteral(1);
    DomainObjectTypeVisitor v = new DomainObjectTypeVisitor();
    Type expected = PrimitiveType.INTEGER;
    Type actual = e.accept(v);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testVisitStringLiteral() {
    DomainObject e = new StringLiteral("foo");
    DomainObjectTypeVisitor v = new DomainObjectTypeVisitor();
    Type expected = PrimitiveType.STRING;
    Type actual = e.accept(v);
    assertEquals(expected, actual);
  }
  
}
