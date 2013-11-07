package whdl.frontend.ast;

import java.util.HashMap;
import java.util.Map;

public class TupleTypename extends Typename {
	private Map<Identifier, Typename> elements;
	public TupleTypename(){
		this.elements = new HashMap<Identifier, Typename>();
	}
	public Typename get(Identifier s){
		return elements.get(s);
	}
	public void put(Identifier s, Typename t) throws TranslationException {
		if(elements.containsKey(s)){
			throw new DuplicateTupleFieldException(s);
		}
		elements.put(s, t);
	}
}
