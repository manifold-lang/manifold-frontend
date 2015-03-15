package org.manifold.compiler.front;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.manifold.compiler.SchematicValueVisitor;
import org.manifold.compiler.TypeValue;
import org.manifold.compiler.UndefinedBehaviourError;
import org.manifold.compiler.Value;

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
  public void accept(SchematicValueVisitor v) {
    throw new UndefinedBehaviourError(
        "cannot accept non-frontend ValueVisitor into a frontend Value");
  }
  
}
