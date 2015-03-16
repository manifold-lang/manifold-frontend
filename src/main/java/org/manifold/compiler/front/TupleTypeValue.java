package org.manifold.compiler.front;

import java.util.Map;

import org.manifold.compiler.SchematicValueVisitor;
import org.manifold.compiler.TypeValue;
import org.manifold.compiler.UndefinedBehaviourError;

import com.google.common.collect.ImmutableMap;

public class TupleTypeValue extends TypeValue {

  private final Map<String, TypeValue> subtypes;
  // TODO default values

  public Map<String, TypeValue> getSubtypes() {
    return ImmutableMap.copyOf(subtypes);
  }

  public int getSize() {
    return subtypes.size();
  }

  public TypeValue entry(int i){
    return subtypes.get(i);
  }

  public TupleTypeValue(Map<String, TypeValue> subtypes) {
    this.subtypes = subtypes;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("( ");
    for (Map.Entry<String, TypeValue> e : subtypes.entrySet()) {
      String key = e.getKey();
      TypeValue type = e.getValue();
      sb.append(key).append(":");
      if (type == null) {
        sb.append("null");
      } else {
        sb.append(type.toString());
      }
      sb.append(" ");
    }
    sb.append(")");
    return sb.toString();
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (other == null || !(other instanceof TupleTypeValue)) {
      return false;
    }
    TupleTypeValue oTuple = (TupleTypeValue) other;
    if (!(getSize() != oTuple.getSize())) {
      return false;
    }
    // type-check subexpressions for equality
    for (int i = 0; i < getSize(); ++i) {
      TypeValue myType = entry(i).getType();
      TypeValue otherType = oTuple.entry(i).getType();
      if (!myType.equals(otherType)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean isSubtypeOf(TypeValue other) {
    if (this == other) {
      return true;
    }
    if (other == null) {
      return false;
    }
    if (!(other instanceof TupleTypeValue)) {
      return getSupertype().isSubtypeOf(other);
    }
    TupleTypeValue oTuple = (TupleTypeValue) other;
    if (!(getSize() != oTuple.getSize())) {
      return false;
    }
    // type-check subexpressions
    for (int i = 0; i < getSize(); ++i) {
      TypeValue myType = entry(i).getType();
      TypeValue otherType = oTuple.entry(i).getType();
      if (!myType.isSubtypeOf(otherType)) {
        return false;
      }
    }
    return true;

  }

  @Override
  public void accept(SchematicValueVisitor v) {
    throw new UndefinedBehaviourError(
        "cannot accept non-frontend ValueVisitor into a frontend Value");
  }

}
