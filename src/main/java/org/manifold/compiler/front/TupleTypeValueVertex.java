package org.manifold.compiler.front;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class TupleTypeValueVertex extends ExpressionVertex {

  private TupleTypeValue type = null;
  public TupleTypeValue getType() {
    return type;
  }
  
  private Map<String, ExpressionEdge> typeValueEdges;
  public Map<String, ExpressionEdge> getTypeValueEdges() {
    return ImmutableMap.copyOf(typeValueEdges);
  }
  
  private Map<String, ExpressionEdge> defaultValueEdges;
  public Map<String, ExpressionEdge> getDefaultValueEdges() {
    return ImmutableMap.copyOf(defaultValueEdges);
  }
  
  public TupleTypeValueVertex(
      Map<String, ExpressionEdge> typeValueEdges,
      Map<String, ExpressionEdge> defaultValueEdges) {
    this.typeValueEdges = new HashMap<>(typeValueEdges);
    for (ExpressionEdge e : this.typeValueEdges.values()) {
      e.setTarget(this);
    }
    this.defaultValueEdges = new HashMap<>(defaultValueEdges);
    for (ExpressionEdge e : this.defaultValueEdges.values()) {
      e.setTarget(this);
    }
  }

  @Override
  public String toString() {
    if (type == null) {
      return "tuple type value (not elaborated)";
    } else {
      return "tuple type value (" + type.toString() + ")";
    }
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
