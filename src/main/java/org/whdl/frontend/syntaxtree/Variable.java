package org.whdl.frontend.syntaxtree;

public class Variable {
	private VariableIdentifier identifier;
	private Expression typeExpression;

	private boolean assigned = false;
	private Expression valueExpression;

	public Variable(VariableIdentifier identifier, Expression typeExpression) {
		this.identifier = identifier;
		this.typeExpression = typeExpression;
	}

	public VariableIdentifier getIdentifier() {
		return identifier;
	}

	public TypeValue getTypeValue() throws TypeMismatchException {
		// FIXME if it is possible to type-check this expression earlier, it
		// should be done as soon as possible
		// e.g. in a semantic analysis pass
		Value val = typeExpression.evaluate();
		if (!(val instanceof TypeValue)) {
			throw new TypeMismatchException(TypeTypeValue.getInstance(),
					val.getType());
		}
		return (TypeValue) val;
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

}
