package org.whdl.frontend.syntaxtree;

public class VariableNotDefinedException extends Exception {
	private static final long serialVersionUID = 1L;

	private VariableIdentifier id;
	public VariableNotDefinedException(VariableIdentifier id){
		this.id = id;
	}
	@Override
	public String getMessage(){
		return "variable '" + id + "' not defined in this scope";
	}
}
