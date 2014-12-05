package org.manifold.compiler.front;

import java.io.BufferedWriter;
import java.io.IOException;

import org.manifold.compiler.PortTypeValue;
import org.manifold.compiler.TypeTypeValue;
import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

public class PrimitivePortVertex extends ExpressionVertex {

  private final PrimitivePortDefinitionExpression primitivePort;

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

  public PrimitivePortVertex(PrimitivePortDefinitionExpression primitivePort,
      ExpressionEdge signalTypeEdge, ExpressionEdge attributesEdge) {
    this.primitivePort = primitivePort;
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
      return "primitive port (" + primitivePort.toString() + ")";
    }
  }

  public void elaborate(Scope scope) {
 // TODO(murphy)
    throw new UnsupportedOperationException("cannot elaborate primitive port");
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
