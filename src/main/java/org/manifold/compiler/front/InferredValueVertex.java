package org.manifold.compiler.front;

import org.manifold.compiler.InferredTypeValue;
import org.manifold.compiler.InferredValue;
import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

public class InferredValueVertex extends ExpressionVertex {

  @Override
  public TypeValue getType() {
    return new InferredTypeValue(null);
  }

  @Override
  public Value getValue() {
    return new InferredValue((InferredTypeValue) getType());
  }

  public InferredValueVertex(ExpressionGraph g) {
    super(g);
  }

  @Override
  public void writeToDOTFile(BufferedWriter writer) throws IOException {
    String objectID = Integer.toString(System.identityHashCode(this));
    writer.write(objectID);
    writer.write(" [");
    writer.write("label=\"");
    writer.write(objectID);
    writer.write("\n");
    writer.write("infer");
    writer.write("\"");
    writer.write("];");
    writer.newLine();
  }

  @Override
  public ExpressionVertex copy(ExpressionGraph g, Map<ExpressionEdge, ExpressionEdge> edgeMap) {
    return new InferredValueVertex(g);
  }

  @Override
  public void verify() throws Exception {
    // no-op.
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
    // no-op.
  }

}
