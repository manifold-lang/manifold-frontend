package org.whdl.frontend.syntaxtree;

/**
 * 
 * @author Max
 *
 */
public class FunctionInvocationExpression extends Expression
{
	private Expression input;
	private Expression functionExpression;
	private FunctionValue function = null;
	
	public FunctionInvocationExpression(Expression input, Expression function)
	{
		this.input = input;
		this.functionExpression = function;
		
		if(functionExpression.isCompiletimeEvaluable())	{
			try {
				Value exprResult = functionExpression.evaluate();
				this.function = (FunctionValue)exprResult;
			}
			catch (ClassCastException e) {
				// TODO(max) throw a  better exception for this
				// this could definitely be better handled once we get
				// FunctionTypeValue completed
				throw new RuntimeException("Expected expression to evaluate to a function but it did not", e);
			}
		}
		else {
			// should this be invalid?
		}
	}
	
	@Override
    public TypeValue getType()
    {
	    // TODO: what to return when functionExpr cannot be compile time evaluated?
		if(function != null) {
			return function.getType();
		}
		
		// should be an exception?
		return null;
    }

	@Override
    public Value evaluate()
    {
	    // TODO: pending FunctionValue/FunctionTypeValue
		throw new UnsupportedOperationException("FunctionValue/FunctionTypeValue still unfinished");
    }

	@Override
    public void verify()
    {
	    // TODO: pending FunctionValue/FunctionTypeValue
		throw new UnsupportedOperationException("FunctionValue/FunctionTypeValue still unfinished");
    }

	@Override
    public boolean isAssignable()
    {
		return false;
    }

	@Override
    public boolean isCompiletimeEvaluable()
    {
	    return (function != null && function.isCompiletimeEvaluable() && input.isCompiletimeEvaluable());
    }

	@Override
    public boolean isSynthesizable()
    {
	    return true;
    }

}
