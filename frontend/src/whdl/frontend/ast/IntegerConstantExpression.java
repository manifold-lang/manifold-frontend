package whdl.frontend.ast;

public class IntegerConstantExpression extends Expression {
	private int value;
	public IntegerConstantExpression(int value){
		this.value = value;
	}
	public int getValue(){return value;}
}
