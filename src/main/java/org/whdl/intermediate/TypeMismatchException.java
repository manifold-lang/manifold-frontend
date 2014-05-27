package org.whdl.intermediate;

public class TypeMismatchException extends Exception {
	private static final long serialVersionUID = 7730618233489002412L;
	private Type expected;
	private Type actual;
	
	public TypeMismatchException(Type expected, Type actual){
		this.expected = expected;
		this.actual = actual;
	}
	
	public Type getExpectedType(){
	  return this.expected;
	}
	public Type getActualType(){
	  return this.actual;
	}
	
	@Override
	public String getMessage(){
		return "type error: expected '" + expected + "', actual '" + actual + "'";
	}
}
