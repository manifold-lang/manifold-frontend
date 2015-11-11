package org.manifold.compiler.front;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.manifold.compiler.TypeValue;
import org.manifold.compiler.UndefinedBehaviourError;
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

  public VariableIdentifier getId() {
    return id;
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
    return new VariableReferenceVertex(g, id);
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
      throw new UndefinedBehaviourError("variable '" + id + "' not assigned");
    } else { 
      // multiply assigned/
      try {
        g.writeDOTFile(new File("g.yolo.dot"));
      } catch (java.io.IOException err) {

      }
      throw new UndefinedBehaviourError("variable '" + id + 
          "' multiply assigned");
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
