package org.whdl.frontend.syntaxtree;


/**
 * An expression composed a single literal value
 * @author Max
 *
 */
public class LiteralExpression extends Expression
{
	private Value value;
	
	public LiteralExpression(Value value)
	{
		this.value = value;
	}

	@Override
	public TypeValue resultType()
	{
		return value.getType();
	}

	@Override
	public Value evaluate()
	{
		return value;
	}

	@Override
	public void verify()
	{
		// Note (max) - anything else needed for this?
		value.verify();
	}

	@Override
	public boolean isAssignable()
	{
		return false;
	}

	@Override
	public boolean isCompiletimeEvaluable()
	{
		return true;
	}

	@Override
	public boolean isRuntimeEvaluable()
	{
		return true;
	}

}
