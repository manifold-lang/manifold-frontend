package whdl.frontend.ast;

public class AssignStatement extends Statement {
	private Identifier id;
	private Expression expr;
	public AssignStatement(Identifier id, Expression expr){
		this.id = id;
		this.expr = expr;
	}
	public Identifier getID(){return id;}
	public Expression getExpr(){return expr;}
}
