package org.manifold.compiler.front;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

import com.google.common.base.Preconditions;

public class StaticAttributeAccessVertex extends ExpressionVertex {

  private static Logger log = LogManager.getLogger(
      "StaticAttributeAccessVertex");

  private final ExpressionEdge exprEdge;
  public ExpressionEdge getExpressionEdge() {
    return this.exprEdge;
  }

  private final String attributeID;
  public String getAttributeID() {
    return this.attributeID;
  }

  public StaticAttributeAccessVertex(ExpressionGraph exprGraph,
      ExpressionEdge exprEdge, String attrID) {
    super(exprGraph);
    this.exprEdge = exprEdge;
    this.exprEdge.setTarget(this);
    this.exprEdge.setName("expr");
    this.attributeID = attrID;
  }

  private TypeValue type = null;

  @Override
  public TypeValue getType() {
    return type;
  }

  private Value value = null;

  @Override
  public Value getValue() {
    return this.value;
  }

  @Override
  public void elaborate() throws Exception {
    log.debug("elaborating static attribute access");
    ExpressionVertex vExpr = exprEdge.getSource();
    vExpr.elaborate();
    Value val = vExpr.getValue();
    TupleValue tupleValue = (TupleValue) val;
    this.value = tupleValue.entry(attributeID);
    this.type = this.value.getType();
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
    return true;
  }

  @Override
  public String toString() {
    String retval = "attribute[ " + attributeID + " ]";
    if (this.value == null) {
      retval += " (not elaborated)";
    }
    return retval;
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
  public ExpressionVertex copy(ExpressionGraph g,
      Map<ExpressionEdge, ExpressionEdge> edgeMap) {
    Preconditions.checkArgument(edgeMap.containsKey(exprEdge));
    return new StaticAttributeAccessVertex(g,
        edgeMap.get(exprEdge), attributeID);
  }

}
