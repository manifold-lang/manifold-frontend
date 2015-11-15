package org.manifold.compiler.front;

import org.manifold.compiler.SchematicValueVisitor;
import org.manifold.compiler.UndefinedBehaviourError;
import org.manifold.compiler.Value;

public class TupleValue extends Value {

  private final MappedArray<String, Value> entries;

  public MappedArray<String, Value> getEntries() {
    return MappedArray.copyOf(entries);
  }

  public int getSize() {
    return entries.size();
  }

  public Value entry(String key) {
    if (!entries.containsKey(key)) {
      throw new IllegalArgumentException("No value for entry " + key);
    }
    return entries.get(key);
  }

  public Value atIndex(int idx) {
    return entries.get(idx);
  }

  public TupleValue(TupleTypeValue type, MappedArray<String, Value> entries) {
    super(type);
    // TODO check entries against expected types
    this.entries = entries;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("( ");
    for (MappedArray<String, Value>.Entry e : entries) {
      String key = e.getKey();
      Value value = e.getValue();

      sb.append(key).append(":");
      if (value == null) {
        sb.append("null");
      } else {
        sb.append(value.toString());
      }
      sb.append(" ");
    }
    sb.append(")");
    return sb.toString();
  }

  @Override
  public boolean isElaborationtimeKnowable() {
    return true;
  }

  @Override
  public boolean isRuntimeKnowable() {
    return false;
  }

  @Override
  public void accept(SchematicValueVisitor v) {
    throw new UndefinedBehaviourError(
        "cannot accept non-frontend ValueVisitor into a frontend Value");
  }

}
