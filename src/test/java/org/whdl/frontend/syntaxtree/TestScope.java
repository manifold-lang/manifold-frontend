package org.whdl.frontend.syntaxtree;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import org.whdl.frontend.syntaxtree.TestVariable.FacadeTypeExpression;

public class TestScope {

	/*
	 * Most of these tests depend on the correct operation of Variable. Look to
	 * those tests if errors show up.
	 */

	@Test
	public void testGetParentScope() {
		Scope foo = new Scope(null);
		Scope bar = new Scope(foo);
		assertSame(foo, bar.getParentScope());
	}

	private NamespaceIdentifier getNamespaceIdentifier() {
		ArrayList<String> name = new ArrayList<String>(3);
		name.add("whdl");
		name.add("is");
		name.add("cool");

		return new NamespaceIdentifier(name);
	}

	private VariableIdentifier getIdentifier() {
		VariableIdentifier id = new VariableIdentifier(
				getNamespaceIdentifier(), "foo");
		return id;
	}

	// because we just need something that returns a type, without bringing too
	// much else into the works
	class FacadeTypeExpression extends Expression {

		@Override
		public Value evaluate() {
			return TypeTypeValue.getInstance();
		}

		@Override
		public TypeValue getType() {
			return evaluate().getType();
		}

	}

	private Expression getTypeExpression() {
		return new FacadeTypeExpression();
	}

	@Test
	public void testDefineVariable() {
		Scope s = new Scope();
		try {
			s.defineVariable(getIdentifier(), getTypeExpression());
		} catch (MultipleDefinitionException mde) {
			fail("MultipleDefinitionException thrown, but only a single definition was made");
		}
		try {
			s.defineVariable(getIdentifier(), getTypeExpression());
			fail("multiple definitions of variable not detected");
		} catch (MultipleDefinitionException mde) {
			// good
		}
	}

	@Test
	public void testGetVariableType() throws MultipleDefinitionException,
			TypeMismatchException, VariableNotDefinedException {
		// implicitly tests defineVariable()
		Scope s = new Scope();
		s.defineVariable(getIdentifier(), getTypeExpression());
		TypeValue tv = s.getVariableType(getIdentifier());
		assertEquals(getTypeExpression().evaluate(), tv);
	}

	@Test
	public void testGetVariable() throws MultipleDefinitionException {
		// implicitly tests defineVariable()
		Scope s = new Scope();
		try {
			Variable v = s.getVariable(getIdentifier());
			fail("somehow got a variable that doesn't exist yet");
		} catch (VariableNotDefinedException vnde) {
			// good
		}
		s.defineVariable(getIdentifier(), getTypeExpression());
		try {
			Variable v = s.getVariable(getIdentifier());
		} catch (VariableNotDefinedException vnde) {
			fail("could not get a variable defined in this scope");
		}
	}
	
	@Test
	public void testGetVariable_ParentScope() throws MultipleDefinitionException {
		// implicitly tests defineVariable()
		Scope s1 = new Scope();
		Scope s2 = new Scope(s1);
		s1.defineVariable(getIdentifier(), getTypeExpression());
		try{
			Variable v = s2.getVariable(getIdentifier());
		}catch(VariableNotDefinedException vnde){
			fail("could not get a variable defined in a parent scope");
		}
	}

	@Test
	public void testGetVariableValue() throws MultipleDefinitionException, VariableNotDefinedException, MultipleAssignmentException, VariableNotAssignedException {
		// implicitly tests defineVariable(), assignVariable()
		Scope s = new Scope();
		s.defineVariable(getIdentifier(), getTypeExpression());
		s.assignVariable(getIdentifier(), getTypeExpression());
		Value v = s.getVariableValue(getIdentifier());
		assertEquals(getTypeExpression().evaluate(), v);
	}

	@Test
	public void testAssignVariable() throws MultipleDefinitionException,
			MultipleAssignmentException, VariableNotDefinedException {
		// implicitly tests defineVariable()
		Scope s = new Scope();
		try {
			s.assignVariable(getIdentifier(), getTypeExpression());
			fail("assigned to a variable that doesn't exist");
		} catch (VariableNotDefinedException vnde) {
			// good
		}
		s.defineVariable(getIdentifier(), getTypeExpression());
		try {
			s.assignVariable(getIdentifier(), getTypeExpression());
		} catch (MultipleAssignmentException mae) {
			fail("MultipleAssignmentException thrown, but variable only assigned once");
		}
		try {
			s.assignVariable(getIdentifier(), getTypeExpression());
			fail("multiple assignment to a variable was allowed");
		} catch (MultipleAssignmentException mae) {
			// good
		}
	}

}
