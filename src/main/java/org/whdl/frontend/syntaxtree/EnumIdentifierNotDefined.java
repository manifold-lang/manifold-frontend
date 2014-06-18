package org.whdl.frontend.syntaxtree;

public class EnumIdentifierNotDefined extends Exception {
  private static final long serialVersionUID = -5089138470160512850L;
  private EnumTypeValue enumType;
	private String enumIdentifier;

	public EnumIdentifierNotDefined(EnumTypeValue enumType, String enumIdentifier) {
		this.enumType = enumType;
		this.enumIdentifier = enumIdentifier;
	}

	public EnumTypeValue getEnumType() {
		return enumType;
	}

	public String getEnumIdentifier() {
		return enumIdentifier;
	}
}