package org.whdl.frontend.syntaxtree;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class TestEnumTypeValue {
	
	private HashMap<String, Value> enumMap = new HashMap<String, Value>();
	
	@Before
	public void setup() {
		enumMap.clear();
		enumMap.put("foo", BitValue.getInstance(true));
		enumMap.put("bar", BitValue.getInstance(false));
	}

	@Test(expected=TypeMismatchException.class)
	public void testIncorrectType() throws Exception {
		new EnumTypeValue(TypeTypeValue.getInstance(), enumMap).verify();;
	}
	
	@Test(expected=EnumIdentifierNotDefined.class)
	public void testIncorrectIdentifier() throws Exception {
		EnumTypeValue enumType = new EnumTypeValue(BitTypeValue.getInstance(), enumMap);
		enumType.verify();
		EnumValue enumValue = new EnumValue(enumType, "boop");
	}
	
	@Test
	public void testGetters() throws TypeMismatchException {
		EnumTypeValue enumType = new EnumTypeValue(BitTypeValue.getInstance(), enumMap);
		Set<String> enumNames = enumType.getIdentifiers();
		assertTrue(enumNames.containsAll(enumMap.keySet()));
		assertEquals(BitTypeValue.getInstance(), enumType.getEnumsType());
	}
}
