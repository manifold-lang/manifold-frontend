package org.manifold.compiler.front;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

import com.google.common.collect.ImmutableMap;

public class TupleValueVertex extends ExpressionVertex {

  private TupleValue value = null;

  @Override
  public Value getValue() {
    return this.value;
  }

  @Override
  public TypeValue getType() {
    if (value == null) {
      return null;
    } else {
      return value.getType();
    }
  }

  private Map<String, ExpressionEdge> valueEdges;
  public Map<String, ExpressionEdge> getValueEdges() {
    return ImmutableMap.copyOf(valueEdges);
  }

  public TupleValueVertex(ExpressionGraph g,
      Map<String, ExpressionEdge> valueEdges) {
    super(g);
    this.valueEdges = new HashMap<>(valueEdges);
    for (Map.Entry<String, ExpressionEdge> e
        : this.valueEdges.entrySet()) {
      e.getValue().setTarget(this);
      e.getValue().setName(e.getKey());
    }
  }

  @Override
  public String toString() {
    if (value == null) {
      return "tuple value (not elaborated)";
    } else {
      return "tuple value " + value.toString();
    }
  }

  @Override
  public void elaborate() throws Exception {
    Map<String, Value> values = new HashMap<>();
    Map<String, TypeValue> types = new HashMap<>();
    for (Map.Entry<String, ExpressionEdge> entry : valueEdges.entrySet()) {
      ExpressionVertex vSource = entry.getValue().getSource();
      vSource.elaborate();
      values.put(entry.getKey(), vSource.getValue());
      types.put(entry.getKey(), vSource.getType());
    }
    // construct a type
    TupleTypeValue tupleType = new TupleTypeValue(types);
    this.value = new TupleValue(tupleType, values);
  }

  @Override
  public void verify() throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean isElaborationtimeKnowable() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isRuntimeKnowable() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void writeToDOTFile(BufferedWriter writer) throws IOException {
    String objectID = Integer.toString(System.identityHashCode(this));
    String label = this.toString();
    writer.write(objectID);
    writer.write(" [");
    writer.write("label=\"");
    writer.write(objectID);
    writer.write("\n");
    writer.write(label);
    writer.write("\"");
    writer.write("];");
    writer.newLine();
  }

}
