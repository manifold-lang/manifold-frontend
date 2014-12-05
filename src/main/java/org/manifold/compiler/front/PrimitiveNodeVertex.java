package org.manifold.compiler.front;

import java.io.BufferedWriter;
import java.io.IOException;

import org.manifold.compiler.NodeTypeValue;

public class PrimitiveNodeVertex extends ExpressionVertex {

  private final PrimitiveNodeDefinitionExpression primitiveNode;

  private NodeTypeValue node = null;
  public NodeTypeValue getElaboratedNode() {
    return node;
  }

  private ExpressionEdge portTypeEdge;
  public ExpressionEdge getPortTypeEdge() {
    return portTypeEdge;
  }

  private ExpressionEdge attributesEdge;
  public ExpressionEdge getAttributesEdge() {
    return attributesEdge;
  }


  public PrimitiveNodeVertex(PrimitiveNodeDefinitionExpression primitiveNode,
      ExpressionEdge portTypeEdge, ExpressionEdge attributesEdge) {
    this.primitiveNode = primitiveNode;
    this.portTypeEdge = portTypeEdge;
    this.portTypeEdge.setTarget(this);
    this.portTypeEdge.setName("port type");
    this.attributesEdge = attributesEdge;
    this.attributesEdge.setTarget(this);
    this.attributesEdge.setName("attributes");
  }

  @Override
  public String toString() {
    if (node == null) {
      return "primitive node (not elaborated)";
    } else {
      return "primitive port (" + primitiveNode.toString() + ")";
    }
  }

  public void elaborate(Scope scope) {
    // TODO(murphy)
    throw new UnsupportedOperationException("cannot elaborate primitive node");
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
