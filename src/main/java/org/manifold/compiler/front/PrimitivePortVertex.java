package org.manifold.compiler.front;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.manifold.compiler.PortTypeValue;
import org.manifold.compiler.TypeValue;

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
    // TODO look through the edge(s) instead
    TypeValue signalType =
        primitivePort.getTypeValueExpression().evaluate(scope, TypeValue.class);
    // TODO(murphy) attributes
    Map<String, TypeValue> attributes = new HashMap<>();
    port = new PortTypeValue(signalType, attributes);
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
