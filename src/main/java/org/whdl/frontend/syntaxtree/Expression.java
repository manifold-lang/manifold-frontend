package org.whdl.frontend.syntaxtree;


/**
 * Base expr class
 * @author Max
 *
 */
public abstract class Expression
{
	public abstract TypeValue resultType();
	public abstract Value evaluate();
	public abstract void verify();
	public abstract boolean isAssignable();
	
	// Note (max) - I kind of don't like these since it will involve making a check
	// and then acting upon it. Would it be possible to remove one or more of these
	// and replace them with exceptions?
	public abstract boolean isCompiletimeEvaluable();
	public abstract boolean isRuntimeEvaluable();
}
