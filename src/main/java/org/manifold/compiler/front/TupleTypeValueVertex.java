package org.manifold.compiler.front;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Preconditions;
import org.manifold.compiler.TypeTypeValue;
import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

import com.google.common.collect.ImmutableMap;

public class TupleTypeValueVertex extends ExpressionVertex {
  private TupleTypeValue type = null;
  @Override
  public Value getValue() {
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
  
  public TupleTypeValueVertex(ExpressionGraph g,
      Map<String, ExpressionEdge> typeValueEdges,
      Map<String, ExpressionEdge> defaultValueEdges) {
    super(g);
    this.typeValueEdges = new HashMap<>(typeValueEdges);
    for (Map.Entry<String, ExpressionEdge> e 
        : this.typeValueEdges.entrySet()) {
      e.getValue().setTarget(this);
      e.getValue().setName(e.getKey());
    }
    this.defaultValueEdges = new HashMap<>(defaultValueEdges);
    for (Map.Entry<String, ExpressionEdge> e 
        : this.defaultValueEdges.entrySet()) {
      e.getValue().setTarget(this);
      e.getValue().setName(e.getKey());
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

  @Override
  public ExpressionVertex copy(ExpressionGraph g, Map<ExpressionEdge, ExpressionEdge> edgeMap) {
    Map<String, ExpressionEdge> newTypeValueEdges = new HashMap<>();
    Map<String, ExpressionEdge> newDefaultValueEdges = new HashMap<>();
    typeValueEdges.forEach((key, val) -> {
        Preconditions.checkArgument(edgeMap.containsKey(val));
        newTypeValueEdges.put(key, edgeMap.get(val));
      });
    defaultValueEdges.forEach((key, val) -> {
        Preconditions.checkArgument(edgeMap.containsKey(val));
        newDefaultValueEdges.put(key, edgeMap.get(val));
      });
    return new TupleTypeValueVertex(g, newTypeValueEdges, newDefaultValueEdges);
  }

  @Override
  public TypeValue getType() {
    return TypeTypeValue.getInstance();
  }

  @Override
  public void verify() throws Exception {
    // TODO Auto-generated method stub
    
  }

  @Override
  public boolean isElaborationtimeKnowable() {
    return true;
  }

  @Override
  public boolean isRuntimeKnowable() {
    return true;
  }

  @Override
  public void elaborate() throws Exception {  
    Map<String, TypeValue> subtypes = new HashMap<>();
    for (Map.Entry<String, ExpressionEdge> entry : typeValueEdges.entrySet()) {
      ExpressionVertex vSource = entry.getValue().getSource();
      vSource.elaborate();
      TypeValue t = TypeAssertions.assertIsType(vSource.getValue());
      subtypes.put(entry.getKey(), t);
    }
    // TODO default values
    this.type = new TupleTypeValue(subtypes);
  }
  
}
