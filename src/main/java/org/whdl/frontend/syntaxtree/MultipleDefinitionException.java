package org.whdl.frontend.syntaxtree;

public class MultipleDefinitionException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private VariableIdentifier id;
	public MultipleDefinitionException(VariableIdentifier id) {
		this.id = id;
	}
	@Override
	public String getMessage(){
		return "multiple definitions of variable '" + id + "'";
	}

}
