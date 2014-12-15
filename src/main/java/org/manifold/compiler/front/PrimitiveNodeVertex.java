package org.manifold.compiler.front;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.manifold.compiler.NilTypeValue;
import org.manifold.compiler.NodeTypeValue;
import org.manifold.compiler.PortTypeValue;
import org.manifold.compiler.TypeTypeValue;
import org.manifold.compiler.TypeValue;
import org.manifold.compiler.UndefinedBehaviourError;
import org.manifold.compiler.Value;

public class PrimitiveNodeVertex extends ExpressionVertex {

  private NodeTypeValue node = null;
  // TODO getValue() should return a tuple of "port bindings" instead
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
      return "primitive node (" + node.toString() + ")";
    }
  }

  private void extractPortTypes(TypeValue type,
      Map<String, PortTypeValue> portMap) throws TypeMismatchException {
    if (!(type instanceof TupleTypeValue)) {
      Map<String, TypeValue> x = new HashMap<>();
      x.put("x", TypeTypeValue.getInstance());
      throw new TypeMismatchException(
          new TupleTypeValue(x),
          type);
    }
    TupleTypeValue tupleType = (TupleTypeValue) type;
    for (Map.Entry<String, TypeValue> e : tupleType.getSubtypes().entrySet()) {
      // if any type value is NIL, don't construct a port
      if (e.getValue().equals(NilTypeValue.getInstance())) {
        continue;
      }
      if (!(e.getValue() instanceof PortTypeValue)) {
        // we would like to throw a TypeMismatchException
        throw new UndefinedBehaviourError(
            "attempt to construct port with non-port type value");
      }
      PortTypeValue portType = (PortTypeValue) e.getValue();
      if (portMap.containsKey(e.getKey())) {
        throw new UndefinedBehaviourError(
            "duplicate port '" + e.getKey() + "' on node");
      } else {
        portMap.put(e.getKey(), portType);
      }
    }
  }

  @Override
  public void elaborate() throws Exception {
    if (node != null) {
      return;
    }
    Map<String, PortTypeValue> portTypeMap = new HashMap<>();
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

    // check that both the input type and output type are tuple typevalues
    // whose entries are PortTypeValue, then extract the identifier-typevalue
    // pairs and add them to the port type map
    extractPortTypes(portType.getInputType(), portTypeMap);
    extractPortTypes(portType.getOutputType(), portTypeMap);

    Map<String, TypeValue> attributesMap = new HashMap<>();
    ExpressionVertex attributesVertex = attributesEdge.getSource();
    attributesVertex.elaborate();
    if (!(attributesVertex.getType()
        .isSubtypeOf(TypeTypeValue.getInstance()))) {
      throw new TypeMismatchException(
          TypeTypeValue.getInstance(),
          attributesVertex.getType());
    }

    // check for NIL
    if (!(attributesVertex.getValue()
        .equals(NilTypeValue.getInstance()))) {
      // TODO: evaluate the expression and get the attributes
      throw new UnsupportedOperationException(
          "node with attributes not supported");
    }

    this.node = new NodeTypeValue(attributesMap, portTypeMap);
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
