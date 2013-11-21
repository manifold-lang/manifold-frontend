package org.whdl.frontend.syntaxtree;

import java.util.HashMap;
import java.util.Map;

public class Scope {
  private Scope parentScope;
  private Map<VariableIdentifier, Variable> symbolTable = new HashMap<VariableIdentifier, Variable>();

  public Scope(Scope parentScope) {
    this.parentScope = parentScope;
  }

  public Scope() {
    this.parentScope = null;
  }

  public Scope getParentScope() {
    return parentScope;
  }

  public void defineVariable(VariableIdentifier identifier,
      Expression typeExpression) throws MultipleDefinitionException {
    // FIXME this does not handle namespaces correctly
    if (symbolTable.containsKey(identifier)) {
      // FIXME this does not handle functions with different type
      // signatures correctly
      throw new MultipleDefinitionException(identifier);
    }
    // naturally, variable shadowing is allowed -- "closer" scopes hide
    // "further" ones
    Variable v = new Variable(identifier, typeExpression);
    symbolTable.put(identifier, v);
  }

  public boolean containsIdentifier(VariableIdentifier identifier) {
    // FIXME does it make sense for this to search recursively upwards?
    // i.e. do we always want to check every scope or just this one?
    if (symbolTable.containsKey(identifier)) {
      return true;
    } else if (parentScope == null) {
      return false;
    } else {
      return parentScope.containsIdentifier(identifier);
    }
  }

  public TypeValue getVariableType(VariableIdentifier identifier)
      throws TypeMismatchException, VariableNotDefinedException {
    return getVariable(identifier).getType();
  }

  public Variable getVariable(VariableIdentifier identifier)
      throws VariableNotDefinedException {
    // FIXME this does not handle namespaces correctly
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
    return getVariable(identifier).getValue();
  }

  public void assignVariable(VariableIdentifier identifier,
      Expression valueExpression) throws VariableNotDefinedException,
      MultipleAssignmentException {
    Variable v = getVariable(identifier);
    v.setValue(valueExpression);
  }

}
