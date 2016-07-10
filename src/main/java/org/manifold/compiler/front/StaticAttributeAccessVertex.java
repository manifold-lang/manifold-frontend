package org.manifold.compiler.front;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

import com.google.common.base.Preconditions;

abstract class StaticAttributeAccessVertex extends ExpressionVertex {

  private static Logger log = LogManager.getLogger(
      "StaticAttributeAccessVertex");

  protected final ExpressionEdge exprEdge;
  public ExpressionEdge getExpressionEdge() {
    return this.exprEdge;
  }

  protected StaticAttributeAccessVertex(ExpressionGraph exprGraph,
                                     ExpressionEdge exprEdge) {
    super(exprGraph);
    this.exprEdge = exprEdge;
    this.exprEdge.setTarget(this);
    this.exprEdge.setName("expr");
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

  protected abstract Value getValEntry(Value v) throws Exception;

  protected abstract TypeValue getTypeEntry(TypeValue t) throws Exception;

  /**
   * The string representation of the value used to access this attribute
   */
  protected abstract String attributeToString();

  @Override
  public void elaborate() throws Exception {
    log.debug("elaborating static attribute access");
    ExpressionVertex vExpr = exprEdge.getSource();
    vExpr.elaborate();

    Value val = vExpr.getValue();
    this.value = getValEntry(val);

    TypeValue t = vExpr.getType();
    this.type = getTypeEntry(t);
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
    String retval = "attribute[ " + attributeToString() + " ]";
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
}

class StaticStringAttributeAccessVertex extends StaticAttributeAccessVertex {

  private final String attributeID;

  public String getAttributeID() {
    return this.attributeID;
  }

  @Override
  protected String attributeToString() {
    return this.attributeID;
  }

  @Override
  protected final Value getValEntry(Value v) throws Exception {
    if (!(v instanceof NamedEntryValue)) {
      throw new RuntimeException("Cannot get entry by attribute of " + v.toString());
    }
    return ((NamedEntryValue) v).getEntry(attributeID);
  }

  @Override
  protected final TypeValue getTypeEntry(TypeValue t) throws Exception {
    if (!(t instanceof NamedEntryTypeValue)) {
      throw new RuntimeException("Cannot get entry by attribute of " + t.toString());
    }
    return ((NamedEntryTypeValue) t).getEntry(attributeID);
  }

  public StaticStringAttributeAccessVertex(ExpressionGraph exprGraph,
                                     ExpressionEdge exprEdge, String attrID) {
    super(exprGraph, exprEdge);
    this.attributeID = attrID;
  }

  @Override
  public ExpressionVertex copy(ExpressionGraph g,
                               Map<ExpressionEdge, ExpressionEdge> edgeMap) {
    Preconditions.checkArgument(edgeMap.containsKey(exprEdge));
    return new StaticStringAttributeAccessVertex(g,
            edgeMap.get(exprEdge), attributeID) {
    };
  }
}

class StaticNumberAttributeAccessVertex extends StaticAttributeAccessVertex {

  private final int attributeIDX;

  public final int getAttributeIDX() {
    return this.attributeIDX;
  }

  @Override
  protected String attributeToString() {
    return Integer.toString(this.attributeIDX);
  }

  @Override
  protected final Value getValEntry(Value v) {
    if (!(v instanceof TupleValue)) {
      throw new RuntimeException("Cannot get entry by index of " + v.toString());
    }
    return ((TupleValue) v).getEntry(attributeIDX);
  }

  @Override
  protected final TypeValue getTypeEntry(TypeValue t) throws Exception {
    if (!(t instanceof TupleTypeValue)) {
      throw new RuntimeException("Cannot get entry by attribute of " + t.toString());
    }
    return ((TupleTypeValue) t).getEntry(attributeIDX);
  }

  public StaticNumberAttributeAccessVertex(ExpressionGraph exprGraph,
                                           ExpressionEdge exprEdge, int attrIDX) {
    super(exprGraph, exprEdge);
    this.attributeIDX = attrIDX;
  }

  @Override
  public ExpressionVertex copy(ExpressionGraph g,
                               Map<ExpressionEdge, ExpressionEdge> edgeMap) {
    Preconditions.checkArgument(edgeMap.containsKey(exprEdge));
    return new StaticNumberAttributeAccessVertex(g,
            edgeMap.get(exprEdge), attributeIDX) {
    };
  }
}
