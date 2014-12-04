package org.manifold.compiler.front;

import java.util.Map;

import org.manifold.compiler.SchematicValueVisitor;
import org.manifold.compiler.TypeValue;
import org.manifold.compiler.UndefinedBehaviourError;

public class TupleTypeValue extends TypeValue {

  private final Map<String, TypeValue> subtypes;
  
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
    if (v instanceof FrontendValueVisitor) {
      FrontendValueVisitor visitor = (FrontendValueVisitor) v;
      visitor.visit(this);
    } else {
      throw new UndefinedBehaviourError(
          "cannot accept non-frontend ValueVisitor into a frontend Value");
    }
  }
  
}
