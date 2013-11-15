package org.whdl.frontend.syntaxtree;

public class Variable {
	private VariableIdentifier identifier;
	private Expression typeExpression;

	public Variable(VariableIdentifier identifier, Expression typeExpression){
		this.identifier = identifier;
		this.typeExpression = typeExpression;
	}
	public VariableIdentifier getIdentifier(){return identifier;}
	public TypeValue getTypeValue(){
		Value val = typeExpression.eval();
		if(!(val instanceof TypeValue)){
			// FIXME type error
		}
		return (TypeValue)val;
	}
	
	private boolean assigned = false;
	private Expression valExpr;
	
	public boolean isAssigned(){return assigned;}
	public Value getValue(){
		if(!isAssigned()){
			// FIXME variable not assigned exception
		}
		return valExpr.eval();
	}
	public void setValue(Expression valExpr){
		if(isAssigned()){
			// FIXME multiple assignment exception 
		}
		this.valExpr = valExpr;
		this.assigned = true;
	}
	
}
