package whdl.frontend.ast;

public class AtomicTypename extends Typename {
	private Identifier name;
	public AtomicTypename(Identifier name){
		this.name = name;
	}
	public Identifier getName(){return name;}
}
