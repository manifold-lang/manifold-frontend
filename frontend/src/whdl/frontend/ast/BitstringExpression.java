package whdl.frontend.ast;

public class BitstringExpression extends Expression {
	private String bits;
	public BitstringExpression(String bits){
		this.bits = bits;
	}
	public String getBits(){return bits;}
}
