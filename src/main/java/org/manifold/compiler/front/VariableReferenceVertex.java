package org.manifold.compiler.front;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

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

  private TypeValue type = null;
  private Value value = null;
  
  private ExpressionEdge findAssigningEdge() {
    ExpressionGraph g = getExpressionGraph();
    List<ExpressionEdge> incoming = g.getEdgesToTarget(this);
    if (incoming.size() == 1) {
      return incoming.get(0);
    } else if (incoming.size() == 0) {
      // not assigned
      // TODO we would like to throw this exception but we need a Variable
      //throw new VariableNotAssignedException(id);
      throw new UndefinedBehaviourError("variable '" + id + "' not assigned");
    } else { 
      // multiply assigned
   // TODO we would like to throw this exception but we need a Variable
      //throw new MultipleAssignmentException(id);
      throw new UndefinedBehaviourError("variable '" + id + 
          "' multiply assigned");
    }
  }
  
  @Override
  public TypeValue getType() {
    if (type == null) {
      ExpressionEdge e = findAssigningEdge();
      type = e.getSource().getType();
    }
    return type;
  }

  @Override
  public Value getValue() {
    if (value == null) {
      ExpressionEdge e = findAssigningEdge();
      value = e.getSource().getValue();
    }
    return value;
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
