package whdl.frontend.ast;

public class TupleFieldExpression extends Expression {
	private Expression tupleExpr;
	private Expression fieldID;
	public TupleFieldExpression(Expression tupleExpr, Expression fieldID){
		this.tupleExpr = tupleExpr;
		this.fieldID = fieldID;
	}
	public Expression getTupleExpr(){return tupleExpr;}
	public Expression getFieldID(){return fieldID;}
}
