package org.manifold.compiler.front;

import com.google.common.base.Preconditions;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

public class TupleValueVertex extends ExpressionVertex {

  private static Logger log = LogManager.getLogger("TupleValueVertex");

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

  private MappedArray<String, ExpressionEdge> valueEdges;
  public MappedArray<String, ExpressionEdge> getValueEdges() {
    // Return a clone so valueEdges we want an "immutable" version
    return MappedArray.copyOf(valueEdges);
  }

  public TupleValueVertex(ExpressionGraph g,
      MappedArray<String, ExpressionEdge> valueEdges) {
    super(g);
    this.valueEdges = MappedArray.copyOf(valueEdges);
    for (MappedArray<String, ExpressionEdge>.Entry e
        : this.valueEdges) {
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
    log.debug("elaborating tuple value");

    MappedArray<String, Value> values = new MappedArray<>();
    MappedArray<String, TypeValue> types = new MappedArray<>();
    for (MappedArray<String, ExpressionEdge>.Entry entry : valueEdges) {
      log.debug("elaborating tuple entry '" + entry.getKey() + "'");
      ExpressionVertex vSource = entry.getValue().getSource();
      vSource.elaborate();

      // If the source was a function invocation then the source edge will now point to something else, probably a node
      // query the source again to get the updated source
      vSource = entry.getValue().getSource();
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

  @Override
  public ExpressionVertex copy(ExpressionGraph g, Map<ExpressionEdge, ExpressionEdge> edgeMap) {
    MappedArray<String, ExpressionEdge> newValueEdges = new MappedArray<>();
    for (MappedArray<String, ExpressionEdge>.Entry entry : valueEdges) {
      Preconditions.checkArgument(edgeMap.containsKey(entry.getValue()));
      newValueEdges.put(entry.getKey(), edgeMap.get(entry.getValue()));
    }
    return new TupleValueVertex(g, newValueEdges);
  }

}
