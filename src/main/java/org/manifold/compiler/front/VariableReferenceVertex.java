package org.manifold.compiler.front;

import java.io.BufferedWriter;
import java.io.IOException;

public class VariableReferenceVertex extends ExpressionVertex {
  private VariableIdentifier id;
  public VariableIdentifier getIdentifier() {
    return id;
  }
  
  public VariableReferenceVertex(VariableIdentifier id) {
    this.id = id;
  }
  
  @Override
  public String toString() {
    return "var " + id.toString();
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
