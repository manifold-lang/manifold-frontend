package org.manifold.compiler.front;

import java.io.BufferedWriter;
import java.io.IOException;

import org.manifold.compiler.NodeTypeValue;
import org.manifold.compiler.TypeTypeValue;
import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

public class PrimitiveNodeVertex extends ExpressionVertex {

  private final PrimitiveNodeDefinitionExpression primitiveNode;

  private NodeTypeValue node = null;
  @Override
  public Value getValue() {
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


  public PrimitiveNodeVertex(ExpressionGraph g,
      PrimitiveNodeDefinitionExpression primitiveNode,
      ExpressionEdge portTypeEdge, ExpressionEdge attributesEdge) {
    super(g);
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

  public void elaborate() throws TypeMismatchException {
    if (node != null) {
      return;
    }
    ExpressionVertex portTypeVertex = portTypeEdge.getSource();
    // must be (Type -> Type)
    TypeValue typeConstructor = new FunctionTypeValue(
        TypeTypeValue.getInstance(), TypeTypeValue.getInstance());
    if (!(portTypeVertex.getType().equals(typeConstructor))) {
      throw new TypeMismatchException(
          typeConstructor, portTypeVertex.getType());
    }
    FunctionTypeValue portType = (FunctionTypeValue) portTypeVertex.getType();
    // TODO finish up here
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
