package org.manifold.compiler.front;

import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;
import java.util.Objects;

public class EnumValue extends Value {

  private final String identifier;
  private Value value;

  public EnumValue(EnumTypeValue type, String identifier)
      throws EnumIdentifierNotDefined {
    super(type);
    assert type instanceof EnumTypeValue;
    this.identifier = identifier;

    if (!type.contains(identifier)) {
      throw new EnumIdentifierNotDefined(
          (EnumTypeValue) this.getType(),
          this.identifier
      );
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
  public void verify() throws Exception {
    value.verify();
  }

  @Override
  public boolean isElaborationtimeKnowable() {
    return value.isElaborationtimeKnowable();
  }

  @Override
  public boolean isRuntimeKnowable() {
    return value.isRuntimeKnowable();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof EnumValue) {
      EnumValue otherEnum = (EnumValue) obj;
      if (getType().equals(otherEnum.getType()) &&
          identifier.equals(otherEnum.identifier)) {
        return true;
      }
    }

    return getValue().equals(obj);
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 73 * hash + Objects.hashCode(this.getType());
    hash = 73 * hash + Objects.hashCode(this.identifier);
    return hash;
  }
}
