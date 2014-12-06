package org.manifold.compiler.front;

import java.io.BufferedWriter;
import java.io.IOException;

import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

public class ConstantValueVertex extends ExpressionVertex {

  private final Value value;
  @Override
  public Value getValue() {
    return this.value;
  }
  public ConstantValueVertex (ExpressionGraph g, Value value) {
    super(g);
    this.value = value;
  }

  @Override
  public void writeToDOTFile(BufferedWriter writer) throws IOException {
    String objectID = Integer.toString(System.identityHashCode(this));
    String label = getValue().toString();
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
  public TypeValue getType() {
    return this.value.getType();
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

}
