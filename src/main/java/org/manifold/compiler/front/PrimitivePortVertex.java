package org.manifold.compiler.front;

import java.util.HashMap;
import java.util.Map;

import org.manifold.compiler.PortTypeValue;
import org.manifold.compiler.TypeValue;

public class PrimitivePortVertex extends ExpressionVertex {

  private final PrimitivePortTypeValue primitivePort;

  private PortTypeValue port = null;
  public PortTypeValue getElaboratedPort() {
    return port;
  }

  public PrimitivePortVertex(PrimitivePortTypeValue primitivePort) {
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

}
