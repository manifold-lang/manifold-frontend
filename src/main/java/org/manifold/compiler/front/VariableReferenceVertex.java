package org.manifold.compiler.front;

import java.io.BufferedWriter;
import java.io.IOException;

import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

public class VariableReferenceVertex extends ExpressionVertex {
  private VariableIdentifier id;
  public VariableIdentifier getIdentifier() {
    return id;
  }
  
  public VariableReferenceVertex(ExpressionGraph g, VariableIdentifier id) {
    super(g);
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

  @Override
  public TypeValue getType() {
    // TODO
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public Value getValue() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("not implemented");
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
}
