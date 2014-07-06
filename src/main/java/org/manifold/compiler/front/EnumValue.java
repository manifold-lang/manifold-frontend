package org.manifold.compiler.front;

import java.util.Objects;

public class EnumValue extends Value {

  private final EnumTypeValue type;
  private final String identifier;
  private Value value;

  public EnumValue(EnumTypeValue type, String identifier)
      throws EnumIdentifierNotDefined {
    this.type = type;
    this.identifier = identifier;

    if (!type.contains(identifier)) {
      throw new EnumIdentifierNotDefined(this.type, this.identifier);
    }

    value = type.get(identifier);
  }

  public String getIdentifier() {
    return identifier;
  }

  public Value getValue() {
    return value;
  }

  @Override
  public TypeValue getType() {
    return type;
  }

  @Override
  public void verify() throws Exception {
    value.verify();
  }

  @Override
  public boolean isCompiletimeEvaluable() {
    return value.isCompiletimeEvaluable();
  }

  @Override
  public boolean isSynthesizable() {
    return value.isSynthesizable();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof EnumValue) {
      EnumValue otherEnum = (EnumValue) obj;
      if (type.equals(otherEnum.type) &&
          identifier.equals(otherEnum.identifier)) {
        return true;
      }
    }

    return getValue().equals(obj);
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 73 * hash + Objects.hashCode(this.type);
    hash = 73 * hash + Objects.hashCode(this.identifier);
    return hash;
  }
}
