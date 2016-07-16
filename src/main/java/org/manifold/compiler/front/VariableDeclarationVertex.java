package org.manifold.compiler.front;

import org.manifold.compiler.TypeTypeValue;
import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

public class VariableDeclarationVertex extends VariableReferenceVertex {
  private VariableIdentifier id;
  private TypeValue type;
  private Value value;
  private ExpressionVertex vType;

  public VariableDeclarationVertex(ExpressionGraph g, VariableIdentifier id, ExpressionVertex type) {
    super(g, id);
    this.id = id;
    this.vType = type;
    this.type = null;
    this.value = null;
  }

  @Override
  public TypeValue getType() {
    return type;
  }

  @Override
  public Value getValue() {
    if (value == null) {
      return super.getValue();
    }
    return value;
  }

  @Override
  public void verify() throws Exception {
    super.verify();
    this.elaborate();
  }

  @Override
  public void elaborate() throws Exception {
    super.elaborate();
    type = super.getType();
    vType.elaborate();
    Value value = vType.getValue();
    TypeValue declaredType = TypeAssertions.assertIsType(value);
    // check if assigned value is of the correct declaredType
    if (!declaredType.isSubtypeOf(type)) {
      throw new TypeMismatchException(declaredType, type);
    }

    type = declaredType;

    // if vType == 'Type'
    if (value == TypeTypeValue.getInstance()) {
      TypeValue assignedType = TypeAssertions.assertIsType(super.getValue());
      this.value = new TypeVariable(id, assignedType);
    }
  }
}
