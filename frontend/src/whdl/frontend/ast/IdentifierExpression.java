package whdl.frontend.ast;

public class IdentifierExpression extends Expression {
	private Identifier id;
	public IdentifierExpression(Identifier id){
		this.id = id;
	}
	public Identifier getID(){return id;}
}
