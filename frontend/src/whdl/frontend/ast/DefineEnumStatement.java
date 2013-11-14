package whdl.frontend.ast;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DefineEnumStatement extends Statement {
	private Identifier name;
	private Typename baseType;
	private Map<Identifier, Expression> enumPairs;
	
	public DefineEnumStatement(Identifier name, Typename baseType){
		this.name = name;
		this.baseType = baseType;
		this.enumPairs = new HashMap<Identifier, Expression>();
	}
	
	public Identifier getName(){return name;}
	public Typename getBaseType(){return baseType;}
	public Map<Identifier, Expression> getEnumPairs(){return enumPairs;}
	public Set<Identifier> getEnumIdentifiers(){return enumPairs.keySet();}
	public void addEnumPair(Identifier i, Expression e) throws DuplicateEnumExpression{
		if(enumPairs.containsKey(i)){
			throw new DuplicateEnumExpression(i);
		}
		enumPairs.put(i, e);
	}
}
