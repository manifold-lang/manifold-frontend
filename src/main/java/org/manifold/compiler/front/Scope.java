package org.manifold.compiler.front;

import org.manifold.compiler.Value;
import org.manifold.compiler.TypeValue;
import java.util.HashMap;
import java.util.Map;

public class Scope {
  private final Scope parentScope;
  private final Map<VariableIdentifier, Variable> symbolTable;

  public Scope(Scope parentScope) {
    this.symbolTable = new HashMap<>();
    this.parentScope = parentScope;
  }

  public Scope() {
    this.symbolTable = new HashMap<>();
    this.parentScope = null;
  }

  public Scope getParentScope() {
    return parentScope;
  }

  public void defineVariable(VariableIdentifier identifier,
      Expression typeExpression) throws MultipleDefinitionException {
    // TODO this does not handle namespaces correctly
    if (symbolTable.containsKey(identifier)) {
      // TODO this does not handle functions with different type
      // signatures correctly
      throw new MultipleDefinitionException(identifier);
    }
    // naturally, variable shadowing is allowed -- "closer" scopes hide
    // "further" ones
    Variable v = new Variable(identifier, typeExpression);
    symbolTable.put(identifier, v);
  }

  public boolean isVariableDefined(VariableIdentifier identifier) {
    if (symbolTable.containsKey(identifier)) {
      return true;
    } else if (parentScope == null) {
      return false;
    } else {
      return parentScope.isVariableDefined(identifier);
    }
  }

  public TypeValue getVariableType(VariableIdentifier identifier)
      throws TypeMismatchException, VariableNotDefinedException {
    return getVariable(identifier).getType(this);
  }

  public Variable getVariable(VariableIdentifier identifier)
      throws VariableNotDefinedException {
    // TODO this does not handle namespaces correctly
    Variable v = symbolTable.get(identifier);
    if (v == null) {
      // no such variable in this scope; check parent scope
      if (parentScope == null) {
        throw new VariableNotDefinedException(identifier);
      } else {
        return parentScope.getVariable(identifier);
      }
    } else {
      return v;
    }
  }

  public Value getVariableValue(VariableIdentifier identifier)
      throws VariableNotAssignedException, VariableNotDefinedException {
    return getVariable(identifier).getValue(this);
  }

  public void assignVariable(VariableIdentifier identifier,
      Expression valueExpression) throws VariableNotDefinedException,
      MultipleAssignmentException {
    Variable v = getVariable(identifier);
    v.setValueExpression(valueExpression);
  }

}
