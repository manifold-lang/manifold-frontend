package whdl.frontend.ast;

public class FunctionEvalExpression extends Expression {
	private Identifier functionName;
	private Expression inputExpr;
	public FunctionEvalExpression(Identifier functionName, Expression inputExpr){
		this.functionName = functionName;
		this.inputExpr = inputExpr;
	}
	public Identifier getFunctionName(){return functionName;}
	public Expression getInputExpr(){return inputExpr;}
}
