package org.manifold.compiler.front;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.manifold.compiler.SchematicValueVisitor;
import org.manifold.compiler.UndefinedBehaviourError;
import org.manifold.compiler.Value;

import com.google.common.collect.ImmutableMap;

public class TupleValue extends Value {

  // TODO: Make entries optional for array-like tuples.
  private final Map<String, Value> entries;
  private final List<Value> ordered;
  // TODO the order of labels is important
  public Map<String, Value> getEntries() {
    return ImmutableMap.copyOf(entries);
  }

  public int getSize() {
    return entries.size();
  }

  public Value entry(String key) {
    return entries.get(key);
  }

  public Value atIndex(int idx) {
    return ordered.get(idx);
  }

  public TupleValue(TupleTypeValue type, Map<String, Value> values, List<Value> order) {
    super(type);
    // TODO check values against expected types
    this.entries = values;
    this.ordered = order;
  }

  public TupleValue(TupleTypeValue type, LinkedHashMap<String, Value> values) {
    this(type, values, new ArrayList<Value>(values.values()));
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("( ");
    for (Map.Entry<String, Value> e : entries.entrySet()) {
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
