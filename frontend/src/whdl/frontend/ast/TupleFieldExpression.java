package whdl.frontend.ast;

public class TupleFieldExpression extends Expression {
	private Expression tupleExpr;
	private Identifier fieldID;
	public TupleFieldExpression(Expression tupleExpr, Identifier fieldID){
		this.tupleExpr = tupleExpr;
		this.fieldID = fieldID;
	}
	public Expression getTupleExpr(){return tupleExpr;}
	public Identifier getFieldID(){return fieldID;}
}
