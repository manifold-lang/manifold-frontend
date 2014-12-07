package org.manifold.compiler.front;

import java.io.BufferedWriter;
import java.io.IOException;

import org.manifold.compiler.NodeTypeValue;
import org.manifold.compiler.TypeTypeValue;
import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

public class PrimitiveNodeVertex extends ExpressionVertex {

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
      ExpressionEdge portTypeEdge, ExpressionEdge attributesEdge) {
    super(g);
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
      return "primitive port (" + node.toString() + ")";
    }
  }

  public void elaborate() throws Exception {
    if (node != null) {
      return;
    }
    ExpressionVertex portTypeVertex = portTypeEdge.getSource();
    portTypeVertex.elaborate();
    // must be (Type) -> (Type)
    TypeValue typeConstructor = new FunctionTypeValue(
        TypeTypeValue.getInstance(), TypeTypeValue.getInstance());
    if (!(portTypeVertex.getValue() instanceof FunctionTypeValue)) {
      throw new TypeMismatchException(
          typeConstructor, portTypeVertex.getValue());
    }
    FunctionTypeValue portType = (FunctionTypeValue) portTypeVertex.getValue(); 
    if (!(portType.getInputType().isSubtypeOf(TypeTypeValue.getInstance()))) {
      throw new TypeMismatchException(
          typeConstructor, portType);
    }
    if (!(portType.getOutputType().isSubtypeOf(TypeTypeValue.getInstance()))) {
      throw new TypeMismatchException(
          typeConstructor, portType);
    }
    
    throw new UnsupportedOperationException("not finished implementing");
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
