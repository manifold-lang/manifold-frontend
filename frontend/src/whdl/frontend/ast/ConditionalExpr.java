package whdl.frontend.ast;

public class ConditionalExpr extends Expression {
	private Expression whenExpr;
	private Expression posExpr;
	private Expression negExpr;
	public ConditionalExpr(Expression whenExpr, Expression posExpr, Expression negExpr){
		this.whenExpr = whenExpr;
		this.posExpr = posExpr;
		this.negExpr = negExpr;
	}
	public Expression getWhenExpr(){return whenExpr;}
	public Expression getPosExpr(){return posExpr;}
	public Expression getNegExpr(){return negExpr;}
}
