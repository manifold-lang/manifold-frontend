package org.whdl.frontend.syntaxtree;

public class FunctionValue extends Value {

	// Max - barebones FunctionValue class so FunctionInvocationExpression doesn't yell at me
	// finish this later!
	
	public Value evaluate(Value input)	{
		// TODO(max) put stuff here!
		return null;
	}
	
	@Override
	public TypeValue getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void verify() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isCompiletimeEvaluable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSynthesizable() {
		// TODO Auto-generated method stub
		return false;
	}

}
