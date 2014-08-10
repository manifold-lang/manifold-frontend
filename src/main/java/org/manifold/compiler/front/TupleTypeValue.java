package org.manifold.compiler.front;

import java.util.List;

import org.manifold.compiler.TypeValue;

public class TupleTypeValue extends TypeValue {
  // TODO named fields
  // TODO default values
  private final List<TypeValue> subtypes;
  
  public int getSize() {
    return subtypes.size();
  }
  
  public TypeValue entry(int i){
    return subtypes.get(i);
  }
  
  public TupleTypeValue(List<TypeValue> subtypes) {
    this.subtypes = subtypes;
  }
  
  @Override
  public boolean isSubtypeOf(TypeValue other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof TupleTypeValue)) {
      return false;
    }
    TupleTypeValue oTuple = (TupleTypeValue) other;
    if (!(getSize() != oTuple.getSize())) {
      return false;
    }
    // type-check subexpressions
    for (int i = 0; i < getSize(); ++i) {
      TypeValue myType = entry(i).getType();
      TypeValue otherType = oTuple.entry(i).getType();
      if (!(myType.isSubtypeOf(otherType) || otherType.isSubtypeOf(myType))) {
        return false;
      }
    }
    return true;
  }
}
