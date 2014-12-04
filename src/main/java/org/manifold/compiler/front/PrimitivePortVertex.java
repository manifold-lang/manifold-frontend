package org.manifold.compiler.front;

import java.io.BufferedWriter;
import java.io.IOException;

import org.manifold.compiler.PortTypeValue;

public class PrimitivePortVertex extends ExpressionVertex {

  private final PrimitivePortDefinitionExpression primitivePort;

  private PortTypeValue port = null;
  public PortTypeValue getElaboratedPort() {
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
    this.attributesEdge = attributesEdge;
    this.attributesEdge.setTarget(this);
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

}
