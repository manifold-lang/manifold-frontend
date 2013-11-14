package whdl.frontend.ast;

public class DefineTypeStatement extends Statement {
	private Identifier id;
	private Typename type;
	
	public DefineTypeStatement(Identifier id, Typename type){
		this.id = id;
		this.type = type;
	}
	
	public Identifier getID(){return id;}
	public Typename getType(){return type;}
}
