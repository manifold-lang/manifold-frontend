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

  public PrimitivePortVertex(PrimitivePortDefinitionExpression primitivePort) {
    this.primitivePort = primitivePort;
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
