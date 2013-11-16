package org.whdl.frontend.syntaxtree;

public class MultipleAssignmentException extends Exception {
	private static final long serialVersionUID = 1L;

	private Variable var;
	public MultipleAssignmentException(Variable var){
		this.var = var;
	}
	@Override
	public String getMessage(){
		return "multiple assignment to variable '" + var.getIdentifier() + "'";
	}
}
