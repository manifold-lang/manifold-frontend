package org.whdl.intermediate;

public class TypeException extends Exception {
	private static final long serialVersionUID = 7730618233489002412L;
	private String expected;
	private String actual;
	
	public TypeException(String expectedTypename, String actualTypename){
		this.expected = expectedTypename;
		this.actual = actualTypename;
	}
	
	@Override
	public String getMessage(){
		return "type error: expected '" + expected + "', actual '" + actual + "'";
	}
}
