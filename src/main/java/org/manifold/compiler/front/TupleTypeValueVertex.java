package org.manifold.compiler.front;

import com.google.common.base.Preconditions;
import org.manifold.compiler.TypeTypeValue;
import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

public class TupleTypeValueVertex extends ExpressionVertex {
  private TupleTypeValue type = null;
  @Override
  public Value getValue() {
    return type;
  }
  
  private MappedArray<String, ExpressionEdge> typeValueEdges;
  public MappedArray<String, ExpressionEdge> getTypeValueEdges() {
    return MappedArray.copyOf(typeValueEdges);
  }
  
  private MappedArray<String, ExpressionEdge> defaultValueEdges;
  public MappedArray<String, ExpressionEdge> getDefaultValueEdges(){
    return MappedArray.copyOf(defaultValueEdges);
  }
  
  public TupleTypeValueVertex(ExpressionGraph g,
      MappedArray<String, ExpressionEdge> typeValueEdges,
      MappedArray<String, ExpressionEdge> defaultValueEdges) {
    super(g);
    this.typeValueEdges = MappedArray.copyOf(typeValueEdges);
    for (MappedArray<String, ExpressionEdge>.Entry e
        : this.typeValueEdges) {
      e.getValue().setTarget(this);
      e.getValue().setName(e.getKey());
    }
    this.defaultValueEdges = MappedArray.copyOf(defaultValueEdges);
    for (MappedArray<String, ExpressionEdge>.Entry e
        : this.defaultValueEdges) {
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
    MappedArray<String, ExpressionEdge> newTypeValueEdges = new MappedArray<>();
    MappedArray<String, ExpressionEdge> newDefaultValueEdges = new MappedArray<>();
    typeValueEdges.forEach(entry -> {
      Preconditions.checkArgument(edgeMap.containsKey(entry.getValue()));
      newTypeValueEdges.put(entry.getKey(), edgeMap.get(entry.getValue()));
    });
    defaultValueEdges.forEach(entry -> {
      Preconditions.checkArgument(edgeMap.containsKey(entry.getValue()));
      newDefaultValueEdges.put(entry.getKey(), edgeMap.get(entry.getValue()));
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
    MappedArray<String, TypeValue> subtypes = new MappedArray<>();
    for (MappedArray<String, ExpressionEdge>.Entry entry : typeValueEdges) {
      ExpressionVertex vSource = entry.getValue().getSource();
      vSource.elaborate();
      TypeValue t = TypeAssertions.assertIsType(vSource.getValue());
      subtypes.put(entry.getKey(), t);
    }
    // TODO default values
    this.type = new TupleTypeValue(subtypes);
  }
  
}
