package org.whdl.frontend.syntaxtree;

public class VariableNotAssignedException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private Variable var;
	public VariableNotAssignedException(Variable var){
		this.var = var;
	}

	@Override
	public String getMessage(){
		return "value of variable '" + var.getIdentifier() + "' used but not assigned";
	}
}
