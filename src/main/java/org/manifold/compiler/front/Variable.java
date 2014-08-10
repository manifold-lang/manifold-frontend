package org.manifold.compiler.front;

import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;
import org.manifold.compiler.TypeTypeValue;

public class Variable {
  private final VariableIdentifier identifier;
  private final Expression typeExpression;
  private final Scope scope;

  private boolean assigned = false;
  private Expression valueExpression;

  public Variable(
      Scope scope,
      VariableIdentifier identifier,
      Expression typeExpression) {
    this.scope = scope;
    this.identifier = identifier;
    this.typeExpression = typeExpression;
  }

  public VariableIdentifier getIdentifier() {
    return identifier;
  }

  public TypeValue getType() {
    return (TypeValue) typeExpression.getValue(scope);
  }
  
  public Scope getScope() {
    return scope;
  }

  public boolean isAssigned() {
    return assigned;
  }

  public Value getValue() {
    if (!isAssigned()) {
      return null;
    } else {
      return valueExpression.getValue(scope);
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
  
  public void verify() throws TypeMismatchException {
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
    if (assigned && !(valueExpression.getType(scope).isSubtypeOf(getType()))) {
      throw new TypeMismatchException(
          getType(),
          valueExpression.getType(scope)
      );
    }
  }

}
