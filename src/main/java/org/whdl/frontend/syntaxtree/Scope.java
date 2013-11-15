package org.whdl.frontend.syntaxtree;

import java.util.HashMap;
import java.util.Map;

public class Scope {
	private Scope parentScope;
	public Scope(Scope parentScope){
		this.parentScope = parentScope;
	}
	public Scope getParentScope(){return parentScope;}
	
	private Map<VariableIdentifier, Variable> symbolTable = new HashMap<VariableIdentifier, Variable>();
	
	public void defineVariable(VariableIdentifier identifier, Expression typeExpression){
		if(symbolTable.containsKey(identifier)){
			// FIXME multiple definition exception...except for functions with different type signatures
		}
		Variable v = new Variable(identifier, typeExpression);
		symbolTable.put(identifier, v);
	}
	
	public TypeValue getVariableTypeValue(VariableIdentifier identifier){
		return getVariable(identifier).getTypeValue();
	}
	
	public Variable getVariable(VariableIdentifier identifier){
		Variable v = symbolTable.get(identifier);
		if(v == null){
			// no such variable in this scope; check parent scope
			if(parentScope == null){
				// FIXME throw no such definition exception
			}
			return parentScope.getVariable(identifier);
		}else{
			return v;
		}
	}
	
	public Value getVariableValue(VariableIdentifier identifier){
		return getVariable(identifier).getValue();
	}
	
	public void assignVariable(VariableIdentifier identifier, Expression valueExpression){
		Variable v = getVariable(identifier);
		v.setValue(valueExpression);
	}
	
}
