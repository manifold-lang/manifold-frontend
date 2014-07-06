package org.manifold.compiler.front;

public class Variable {
  private final VariableIdentifier identifier;
  private final Expression typeExpression;

  private boolean assigned = false;
  private Expression valueExpression;

  public Variable(VariableIdentifier identifier, Expression typeExpression) {
    this.identifier = identifier;
    this.typeExpression = typeExpression;
  }

  public VariableIdentifier getIdentifier() {
    return identifier;
  }

  public TypeValue getType() throws TypeMismatchException {
    return (TypeValue) typeExpression.evaluate();
  }

  public boolean isAssigned() {
    return assigned;
  }

  public Value getValue() throws VariableNotAssignedException {
    if (!isAssigned()) {
      throw new VariableNotAssignedException(this);
    }
    return valueExpression.evaluate();
  }

  public void setValue(Expression valExpr) throws MultipleAssignmentException {
    if (isAssigned()) {
      throw new MultipleAssignmentException(this);
    }
    this.valueExpression = valExpr;
    this.assigned = true;
  }
  
  public void verify() throws TypeMismatchException {
    // Ensure the Type is an actual type
    if (typeExpression.getType() != TypeTypeValue.getInstance()) {
      // TODO(lucas) We should have a special exception for the case where
      // a nontype value is used as a type.
      throw new TypeMismatchException(
          TypeTypeValue.getInstance(),
          typeExpression.getType()
      );
    }
    
    // Ensure the value is of the correct type
    // TODO(lucas)
    if (assigned && !(valueExpression.getType().isSubtypeOf(getType()))) {
      throw new TypeMismatchException(
          getType(),
          valueExpression.getType()
      );
    }
  }

}
