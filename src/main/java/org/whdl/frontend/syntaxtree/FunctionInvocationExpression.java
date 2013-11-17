package org.whdl.frontend.syntaxtree;

/**
 * 
 * @author Max
 *
 */
public class FunctionInvocationExpression extends Expression
{
	private Expression input;
	private VariableIdentifier function;
	
	public FunctionInvocationExpression(Expression input, VariableIdentifier function)
	{
		// TODO: check that function is actually a function
		// TODO: check that the function takes in the type of input (or do it in verify)?
		// pending because VariableIdentifier doesn't seem to specify Value/FunctionValue
		this.input = input;
		this.function = function;
	}
	
	@Override
    public TypeValue resultType()
    {
		// TODO: VariableIdentifier doesn't specify a Value/FunctionValue?
		return null;
    }

	@Override
    public Value evaluate()
    {
	    // TODO VariableIdentifier doesn't specify a Value/FunctionValue?
	    return null;
    }

	@Override
    public void verify()
    {
	    // TODO: checks in constr or here?
    }

	@Override
    public boolean isAssignable()
    {
	    // TODO: how to determine this?
		return false;
    }

	@Override
    public boolean isCompiletimeEvaluable()
    {
	    // TODO Auto-generated method stub
	    return false;
    }

	@Override
    public boolean isRuntimeEvaluable()
    {
	    // TODO Auto-generated method stub
	    return false;
    }

}
