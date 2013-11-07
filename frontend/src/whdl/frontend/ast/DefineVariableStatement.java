package whdl.frontend.ast;

public class DefineVariableStatement extends Statement {
	private Identifier name;
	private Typename typename;
	private Expression initialValue;
	public DefineVariableStatement(Identifier name, Typename typename){
		this.name = name;
		this.typename = typename;
		// FIXME initialValue?
	}
	public DefineVariableStatement(Identifier name, Typename typename, Expression initialValue){
		this.name = name;
		this.typename = typename;
		this.initialValue = initialValue;
	}
	public Identifier getName(){return name;}
	public Typename getTypename(){return typename;}
	public Expression getInitialValueExpr(){return initialValue;} 
	
}
