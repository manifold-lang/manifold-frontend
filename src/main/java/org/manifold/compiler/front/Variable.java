package org.manifold.compiler.front;

import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

// TODO dead code
public class Variable {
  private final VariableIdentifier identifier;
  private TypeValue type;
  private final Scope scope;

  private boolean assigned = false;
  private Object valueExpression; // was Expression

  public Variable(Scope scope, VariableIdentifier identifier) {
    this.scope = scope;
    this.identifier = identifier;
    this.type = UnknownTypeValue.getInstance();
  }

  public VariableIdentifier getIdentifier() {
    return identifier;
  }

  public TypeValue getType() {
    return this.type;
  }

  public void setType(TypeValue type) {
    this.type = type;
  }

  public Scope getScope() {
    return scope;
  }

  public boolean isAssigned() {
    return assigned;
  }

  public Value getValue() {
    return null;
  }

  public void verify() {
    // TODO(murphy)
    /*
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
    */
  }

}
