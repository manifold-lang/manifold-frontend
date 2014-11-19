package org.manifold.compiler.front;

import org.manifold.compiler.NilTypeValue;
import org.manifold.compiler.SchematicValueVisitor;
import org.manifold.compiler.TypeValue;
import org.manifold.compiler.UndefinedBehaviourError;

public class PrimitivePortTypeValue extends TypeValue {

  // TODO(murphy) figure out where the name of the port type value comes from

  private final Expression typevalueExpr;
  public Expression getTypeValueExpression() {
    return typevalueExpr;
  }

  private final Expression attributesExpr;
  public Expression getAttributesExpression() {
    return attributesExpr;
  }

  public PrimitivePortTypeValue (Expression typevalue) {
    this.typevalueExpr = typevalue;
    this.attributesExpr = new LiteralExpression(NilTypeValue.getInstance());
  }

  public PrimitivePortTypeValue (Expression typevalue, Expression attributes) {
    this.typevalueExpr = typevalue;
    this.attributesExpr = attributes;
  }

  @Override
  public void accept(SchematicValueVisitor v) {
    if (v instanceof FrontendValueVisitor) {
      FrontendValueVisitor visitor = (FrontendValueVisitor) v;
      visitor.visit(this);
    } else {
      throw new UndefinedBehaviourError(
          "cannot accept non-frontend ValueVisitor into a frontend Value");
    }
  }

  @Override
  public boolean isElaborationtimeKnowable() {
    return true;
  }

  @Override
  public boolean isRuntimeKnowable() {
    return false;
  }

}
