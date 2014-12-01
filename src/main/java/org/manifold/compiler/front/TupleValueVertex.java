package org.manifold.compiler.front;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class TupleValueVertex extends ExpressionVertex {

  private TupleValue tuple;
  public TupleValue getValue() {
    return tuple;
  }
  private List<ExpressionEdge> valueEdges;
  public List<ExpressionEdge> getValueEdges() {
    return valueEdges;
  }
  
  public TupleValueVertex(TupleValue tuple, List<ExpressionEdge> valueEdges) {
    this.tuple = tuple;
    this.valueEdges = valueEdges;
  }
  
  @Override
  public String toString() {
    StringBuilder tupleString = new StringBuilder();
    tupleString.append("(");
    TupleTypeValue tupleType = (TupleTypeValue) tuple.getType();
    for (int i = 0; i < tupleType.getSize(); ++i) {
      if (tupleType.entry(i) == null) {
        tupleString.append(" null");
      } else {
        tupleString.append(" " + tupleType.entry(i).toString());
      }
    }
    tupleString.append(" )");
    return tupleString.toString();
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
