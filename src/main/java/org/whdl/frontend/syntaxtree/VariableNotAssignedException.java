package org.whdl.frontend.syntaxtree;

public class VariableNotAssignedException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private Variable variable;
	public VariableNotAssignedException(Variable var){
		this.variable = var;
	}

	@Override
	public String getMessage(){
		return "value of variable '" + variable.getIdentifier() + "' used but not assigned";
	}
}
