package org.whdl.frontend.syntaxtree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

public class TestEnumValue {
	
	private HashMap<String, Value> enumMap = new HashMap<String, Value>();
	
	@Before
	public void setup() {
		enumMap.clear();
		enumMap.put("foo", BitValue.getInstance(true));
		enumMap.put("bar", BitValue.getInstance(false));
	}
	
	@Test
	public void testGetters() throws TypeMismatchException, EnumIdentifierNotDefined {
		EnumTypeValue type = new EnumTypeValue(BitTypeValue.getInstance(), enumMap);
		EnumValue foo1 = new EnumValue(type, "foo");
		assertEquals("foo", foo1.getIdentifier());
		assertEquals(type, foo1.getType());
	}
	
	@Test
	public void testValueInheritedMethods() throws Exception {
		EnumTypeValue type = new EnumTypeValue(BitTypeValue.getInstance(), enumMap);
		EnumValue foo1 = new EnumValue(type, "foo");
		foo1.verify();
		assertEquals(foo1.getValue().isCompiletimeEvaluable(), foo1.isCompiletimeEvaluable());
		assertEquals(foo1.getValue().isSynthesizable(), foo1.isSynthesizable());
	}
	
	@Test
	public void testEnumEquality() throws TypeMismatchException, EnumIdentifierNotDefined {
		EnumTypeValue type = new EnumTypeValue(BitTypeValue.getInstance(), enumMap);
		EnumValue foo1 = new EnumValue(type, "foo");
		EnumValue foo2 = new EnumValue(type, "foo");
		
		assertEquals(foo1, foo2);
		assertEquals(foo1, BitValue.getInstance(true));
		assertNotEquals(foo1, BitValue.getInstance(false));
	}
}
