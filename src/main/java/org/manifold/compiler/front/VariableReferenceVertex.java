package org.manifold.compiler.front;

import org.manifold.compiler.TypeValue;
import org.manifold.compiler.UndefinedBehaviourError;
import org.manifold.compiler.Value;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class VariableReferenceVertex extends ExpressionVertex {
  protected VariableIdentifier id;
  protected boolean exported;
  public VariableIdentifier getIdentifier() {
    return id;
  }
  
  public VariableReferenceVertex(ExpressionGraph g, VariableIdentifier id) {
    this(g, id, false);
  }

  public VariableReferenceVertex(ExpressionGraph g, VariableIdentifier id, boolean exported) {
    super(g);
    this.id = id;
    this.exported = exported;
  }

  public VariableIdentifier getId() {
    return id;
  }

  public boolean getExported() {
    return exported;
  }

  public void setExported(boolean exported) {
    this.exported = exported;
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
  public ExpressionVertex copy(ExpressionGraph g, Map<ExpressionEdge, ExpressionEdge> edgeMap) {
    return new VariableReferenceVertex(g, id, exported);
  }

  private TypeValue type = null;
  private Value value = null;
  
  private ExpressionEdge findAssigningEdge() {
    ExpressionGraph g = getExpressionGraph();
    List<ExpressionEdge> incoming = g.getEdgesToTarget(this);
    if (incoming.size() == 1) {
      return incoming.get(0);
    } else if (incoming.size() == 0) {
      // not assigned
      throw new VariableNotDefinedException(id);
    } else { 
      throw new UndefinedBehaviourError("variable '" + id + "' multiply assigned");
    }
  }
  
  @Override
  public TypeValue getType() {
    return type;
  }

  @Override
  public Value getValue() {
    return value;
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
    return false;
  }

  @Override
  public void elaborate() throws Exception {
    ExpressionEdge e = findAssigningEdge();
    e.getSource().elaborate();
    type = e.getSource().getType();
    value = e.getSource().getValue();
  }
}
