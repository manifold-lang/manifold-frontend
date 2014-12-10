package org.manifold.compiler.front;

import org.manifold.compiler.NilTypeValue;
import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

public class PrimitiveNodeDefinitionExpression extends Expression {
  private final Expression typevalueExpr;
  public Expression getTypeValueExpression() {
    return typevalueExpr;
  }

  private final Expression attributesExpr;
  public Expression getAttributesExpression() {
    return attributesExpr;
  }

  public PrimitiveNodeDefinitionExpression (Expression typevalue) {
    this.typevalueExpr = typevalue;
    this.attributesExpr = new LiteralExpression(NilTypeValue.getInstance());
  }

  public PrimitiveNodeDefinitionExpression (
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
