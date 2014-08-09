package org.manifold.compiler.front;

import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;
import org.manifold.compiler.TypeTypeValue;

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

  public TypeValue getType(Scope scope) {
    return (TypeValue) typeExpression.evaluate(scope);
  }

  public boolean isAssigned() {
    return assigned;
  }

  public Value getValue(Scope scope) {
    if (!isAssigned()) {
      return null;
    } else {
      return valueExpression.evaluate(scope);
    }
  }

  public void setValueExpression(Expression valExpr)
      throws MultipleAssignmentException {
    if (isAssigned()) {
      throw new MultipleAssignmentException(this);
    }
    
    this.valueExpression = valExpr;
    this.assigned = true;
  }
  
  public void verify(Scope scope) throws TypeMismatchException {
    // Ensure the Type is an actual type
    if (typeExpression.getType(scope) != TypeTypeValue.getInstance()) {
      // TODO(lucas) We should have a special exception for the case where
      // a nontype value is used as a type.
      throw new TypeMismatchException(
          TypeTypeValue.getInstance(),
          typeExpression.getType(scope)
      );
    }
    
    // Ensure the value is of the correct type
    // TODO(lucas)
    if (assigned && !(valueExpression.getType(scope).isSubtypeOf(getType(scope)))) {
      throw new TypeMismatchException(
          getType(scope),
          valueExpression.getType(scope)
      );
    }
  }

}
