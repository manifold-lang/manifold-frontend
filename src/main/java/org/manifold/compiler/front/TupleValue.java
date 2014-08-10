package org.manifold.compiler.front;

import java.util.List;

import org.manifold.compiler.Value;

public class TupleValue extends Value {

  // TODO named entries
  private final List<Expression> entries;
  
  public int getSize() {
    return entries.size();
  }
  
  public Expression entry(int i) {
    return entries.get(i);
  }
  
  public TupleValue(TupleTypeValue type, List<Expression> values) {
    super(type);
    this.entries = values;
  }

  @Override
  public boolean isElaborationtimeKnowable() {
    return true;
  }

  @Override
  public boolean isRuntimeKnowable() {
    return false;
  }
  
}
