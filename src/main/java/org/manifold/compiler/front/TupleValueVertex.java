package org.manifold.compiler.front;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class TupleValueVertex extends ExpressionVertex {

  private TupleValue tuple;
  private List<ExpressionEdge> valueEdges;
  
  public TupleValueVertex(TupleValue tuple, List<ExpressionEdge> valueEdges) {
    this.tuple = tuple;
    this.valueEdges = ImmutableList.copyOf(valueEdges);
  }
  
  @Override
  public String toString() {
    StringBuilder tupleString = new StringBuilder();
    tupleString.append("(");
    TupleTypeValue tupleType = (TupleTypeValue) tuple.getType();
    for (int i = 0; i < tupleType.getSize(); ++i) {
      tupleString.append(tupleType.entry(i).toString());
    }
    tupleString.append(" )");
    return tupleString.toString();
  }
  
}
