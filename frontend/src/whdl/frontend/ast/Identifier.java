package whdl.frontend.ast;

public class Identifier {
	private String id;
	public Identifier(String id){
		this.id = id;
	}
	@Override
	public String toString(){return id;}	
}
