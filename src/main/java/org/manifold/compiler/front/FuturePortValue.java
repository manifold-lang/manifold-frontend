package org.manifold.compiler.front;

import org.manifold.compiler.NodeValue;
import org.manifold.compiler.PortTypeValue;
import org.manifold.compiler.PortValue;
import org.manifold.compiler.SchematicValueVisitor;
import org.manifold.compiler.UndeclaredIdentifierException;
import org.manifold.compiler.UndefinedBehaviourError;
import org.manifold.compiler.Value;

public class FuturePortValue extends Value {

  private final NodeValueVertex ref;
  private final String portName;

  public FuturePortValue(NodeValueVertex ref,
      String portName, PortTypeValue type) {
    super(type);
    this.ref = ref;
    this.portName = portName;
  }

  public PortValue getPort() throws UndeclaredIdentifierException {
    NodeValue node = ref.getNodeValue();
    if (node == null) {
      throw new UndefinedBehaviourError(
          "attempt to access future port before node has been constructed");
    }
    return node.getPort(portName);
  }

  @Override
  public void accept(SchematicValueVisitor arg0) {
    throw new UndefinedBehaviourError("future port values cannot be visited");
  }

  @Override
  public boolean isElaborationtimeKnowable() {
    return false;
  }

  @Override
  public boolean isRuntimeKnowable() {
    return false;
  }

}
