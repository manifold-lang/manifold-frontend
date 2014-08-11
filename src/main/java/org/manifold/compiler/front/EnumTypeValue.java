package org.manifold.compiler.front;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;
import org.manifold.compiler.ValueVisitor;

public class EnumTypeValue extends TypeValue {

  private final TypeValue enumsType;
  private final Map<String, Value> enums;

  public EnumTypeValue(TypeValue enumsType, Map<String, Value> enums)
      throws TypeMismatchException {
    this.enumsType = enumsType;
    this.enums = new HashMap<>(enums);
  }

  public boolean contains(String key) {
    return enums.containsKey(key);
  }

  public Value get(String key) {
    return enums.get(key);
  }

  public TypeValue getEnumsType() {
    return enumsType;
  }

  public Set<String> getIdentifiers() {
    return enums.keySet();
  }

  @Override
  public void verify() throws Exception{
    enumsType.verify();
    for (Value v : enums.values()) {
      if (!enumsType.equals(v.getType())) {
        throw new TypeMismatchException(enumsType, v.getType());
      }
      v.verify();
    }
  }

  @Override
  public void accept(ValueVisitor visitor) {
    visitor.visit(this);
  }
  
}
