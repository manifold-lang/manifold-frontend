package org.manifold.compiler.front;

import org.manifold.compiler.NilTypeValue;
import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

public class PrimitivePortDefinitionExpression extends Expression {

  // TODO(murphy) figure out where the name of the port type value comes from

  private final Expression typevalueExpr;
  public Expression getTypeValueExpression() {
    return typevalueExpr;
  }

  private final Expression attributesExpr;
  public Expression getAttributesExpression() {
    return attributesExpr;
  }

  public PrimitivePortDefinitionExpression (Expression typevalue) {
    this.typevalueExpr = typevalue;
    this.attributesExpr = new LiteralExpression(NilTypeValue.getInstance());
  }

  public PrimitivePortDefinitionExpression (
      Expression typevalue, Expression attributes) {
    this.typevalueExpr = typevalue;
    this.attributesExpr = attributes;
  }

  @Override
  public TypeValue getType(Scope scope) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Value getValue(Scope scope) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void verify(Scope scope) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean isAssignable() {
    return false;
  }

  @Override
  public boolean isElaborationtimeKnowable(Scope scope) {
    return true;
  }

  @Override
  public boolean isRuntimeKnowable(Scope scope) {
    return true;
  }

  @Override
  public void accept(ExpressionVisitor visitor) throws Exception {
    visitor.visit(this);
  }

}
