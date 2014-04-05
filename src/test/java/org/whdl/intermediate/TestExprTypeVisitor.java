package org.whdl.intermediate;

import static org.junit.Assert.*;

import org.junit.Test;
import org.whdl.intermediate.types.*;

public class TestExprTypeVisitor {

  @Test
  public void testVisitBooleanLiteral() {
    Expression e = new BooleanLiteral(true);
    ExprTypeVisitor v = new ExprTypeVisitor();
    Type expected = PrimitiveType.BOOLEAN;
    Type actual = e.accept(v);
    assertEquals(expected, actual);
  }

  @Test
  public void testVisitIntegerLiteral() {
    Expression e = new IntegerLiteral(1);
    ExprTypeVisitor v = new ExprTypeVisitor();
    Type expected = PrimitiveType.INTEGER;
    Type actual = e.accept(v);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testVisitStringLiteral() {
    Expression e = new StringLiteral("foo");
    ExprTypeVisitor v = new ExprTypeVisitor();
    Type expected = PrimitiveType.STRING;
    Type actual = e.accept(v);
    assertEquals(expected, actual);
  }
  
}
