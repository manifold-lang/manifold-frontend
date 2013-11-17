package org.whdl.frontend.syntaxtree;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class TestVariable {

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

	private Variable getVariable() {
		// declare "foo" as a variable that stores a Type
		Variable v = new Variable(getIdentifier(), getTypeExpression());
		return v;
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
	public void testGetIdentifier() {
		Variable v = getVariable();
		VariableIdentifier id = getIdentifier();
		assertEquals(v.getIdentifier(), id);
	}

	@Test
	public void testGetTypeValue() throws TypeMismatchException {
		Variable v = getVariable();
		TypeValue tv = getTypeExpression().getType();
		assertEquals(v.getTypeValue(), tv);
	}

	@Test
	public void testGetValue() {
		// implicitly tests Variable.isAssigned(), Variable.setValue()
		Variable v = getVariable();
		assertFalse(
				"newly created variable incorrectly appears to be assigned",
				v.isAssigned());
		try {
			Value val = v.getValue();
			fail("incorrectly obtained the value of an unassigned variable");
		} catch (VariableNotAssignedException vnae) {
			// good, that is the exception we want to see
		}
		// and guess what we can ALSO use as the value expression...
		try {
			v.setValue(getTypeExpression());
		} catch (MultipleAssignmentException mae) {
			fail("MultipleAssignmentException thrown, but multiple assignment did not happen");
		}
		try {
			assertEquals(getTypeExpression().evaluate(), v.getValue());
		} catch (VariableNotAssignedException vnae) {
			fail("VariableNotAssignedException thrown, but variable was assigned");
		}
	}

	@Test
	public void testSetValue() {
		// implicitly tests isAssigned()
		Variable v = getVariable();
		assertFalse(
				"newly created variable incorrectly appears to be assigned",
				v.isAssigned());
		// and guess what we can ALSO use as the value expression...
		try {
			v.setValue(getTypeExpression());
		} catch (MultipleAssignmentException mae) {
			fail("MultipleAssignmentException thrown, but multiple assignment did not happen");
		}
		try {
			v.setValue(getTypeExpression());
			fail("multiple assignment to variable was allowed");
		} catch (MultipleAssignmentException mae) {
			// good, that is the exception we want to see
		}
	}

}
