package org.manifold.compiler.front;

import com.google.common.base.Preconditions;
import org.manifold.compiler.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PrimitivePortVertex extends ExpressionVertex {

  private PortTypeValue port = null;
  @Override
  public Value getValue() {
    return port;
  }

  private ExpressionEdge signalTypeEdge;
  public ExpressionEdge getSignalTypeEdge() {
    return signalTypeEdge;
  }

  private ExpressionEdge attributesEdge;
  public ExpressionEdge getAttributesEdge() {
    return attributesEdge;
  }

  public PrimitivePortVertex(ExpressionGraph g,
      ExpressionEdge signalTypeEdge, ExpressionEdge attributesEdge) {
    super(g);
    this.signalTypeEdge = signalTypeEdge;
    this.signalTypeEdge.setTarget(this);
    this.signalTypeEdge.setName("signal type");
    this.attributesEdge = attributesEdge;
    this.attributesEdge.setTarget(this);
    this.attributesEdge.setName("attributes");
  }

  @Override
  public String toString() {
    if (port == null) {
      return "primitive port (not elaborated)";
    } else {
      return "primitive port (" + port.toString() + ")";
    }
  }

  public void elaborate() throws Exception {
    if (port != null) {
      return;
    }

    ExpressionVertex signalTypeVertex = signalTypeEdge.getSource();
    try {
      signalTypeVertex.elaborate();
    } catch (Exception e) {
      throw new TypeMismatchException(TypeTypeValue.getInstance(), signalTypeVertex.getType());
    }
    // check that the signal type is really a type
    if (!(signalTypeVertex.getType()
        .isSubtypeOf(TypeTypeValue.getInstance()))) {
      throw new TypeMismatchException(
          TypeTypeValue.getInstance(),
          signalTypeVertex.getType());
    }
    TypeValue signalType = (TypeValue) signalTypeVertex.getValue();
    
    Map<String, TypeValue> attributesMap = new HashMap<>();
    ExpressionVertex attributesVertex = attributesEdge.getSource();
    if (!(attributesVertex.getType()
        .isSubtypeOf(TypeTypeValue.getInstance()))) {
      throw new TypeMismatchException(
          TypeTypeValue.getInstance(),
          attributesVertex.getType());
    }
    // check for NIL
    attributesVertex.elaborate();
    if (!(attributesVertex.getValue()
        .equals(NilTypeValue.getInstance()))) {
      throw new UnsupportedOperationException(
          "port with attributes not supported");
    }
    port = new PortTypeValue(signalType, attributesMap);
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
    Preconditions.checkArgument(edgeMap.containsKey(signalTypeEdge) && edgeMap.containsKey(attributesEdge));
    return new PrimitivePortVertex(g, edgeMap.get(signalTypeEdge), edgeMap.get(attributesEdge));
  }

  @Override
  public TypeValue getType() {
    return TypeTypeValue.getInstance();
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
