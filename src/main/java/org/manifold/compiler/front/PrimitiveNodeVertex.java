package org.manifold.compiler.front;

import com.google.common.base.Preconditions;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.manifold.compiler.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

public class PrimitiveNodeVertex extends ExpressionVertex {

  private static Logger log = LogManager.getLogger("PrimitiveNodeVertex");

  private NodeTypeValue node = null;
  @Override
  public Value getValue() {
    return node;
  }

  private FunctionTypeValue instantiationSignature = null;
  @Override
  public TypeValue getType() {
    // because this is a function, return a function type
    return instantiationSignature;
  }

  private ExpressionEdge signatureEdge;
  public ExpressionEdge getSignatureEdge() {
    return signatureEdge;
  }


  public PrimitiveNodeVertex(ExpressionGraph g, ExpressionEdge portTypeEdge) {
    super(g);
    this.signatureEdge = portTypeEdge;
    this.signatureEdge.setTarget(this);
    this.signatureEdge.setName("ports/attrs");
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
      MappedArray<String, PortTypeValue> portMap) throws TypeMismatchException {
    if (!(type instanceof TupleTypeValue)) {
      MappedArray<String, TypeValue> x = new MappedArray<>();
      x.put("x", TypeTypeValue.getInstance());
      throw new TypeMismatchException(
          new TupleTypeValue(x),
          type);
    }
    TupleTypeValue tupleType = (TupleTypeValue) type;
    for (MappedArray<String, TypeValue>.Entry e : tupleType.getSubtypes()) {
      // if any type value is NIL, don't construct a port
      if (e.getValue().equals(NilTypeValue.getInstance())) {
        continue;
      }
      if (!(e.getValue() instanceof PortTypeValue)) {
        // this is an attribute, not a port
        continue;
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

  private void extractAttributes(TypeValue type,
      MappedArray<String, TypeValue> attrMap) throws TypeMismatchException {
    if (!(type instanceof TupleTypeValue)) {
      MappedArray<String, TypeValue> x = new MappedArray<>();
      x.put("x", TypeTypeValue.getInstance());
      throw new TypeMismatchException(
          new TupleTypeValue(x),
          type);
    }
    TupleTypeValue tupleType = (TupleTypeValue) type;
    for (MappedArray<String, TypeValue>.Entry e : tupleType.getSubtypes()) {
      // if any type value is NIL, don't set an attribute
      if (e.getValue().equals(NilTypeValue.getInstance())) {
        continue;
      }
      if (e.getValue() instanceof PortTypeValue) {
        // this is a port, not an attribute
        continue;
      }
      if (attrMap.containsKey(e.getKey())) {
        throw new UndefinedBehaviourError(
            "duplicate attribute '" + e.getKey() + "' on node");
      } else {
        attrMap.put(e.getKey(), e.getValue());
      }
    }
  }

  private FunctionTypeValue constructInstantiationSignature(
      FunctionTypeValue oldSig) {
    MappedArray<String, TypeValue> inputTypes = new MappedArray<>();
    MappedArray<String, TypeValue> outputTypes = new MappedArray<>();

    TupleTypeValue oldInputs = (TupleTypeValue) oldSig.getInputType();
    TupleTypeValue oldOutputs = (TupleTypeValue) oldSig.getOutputType();

    // iterate over the old input types to build part of the new input type.
    // when a non-Port type is encountered, this is an attribute;
    // add it to the input types as-is.
    // when a Port type is encountered, first check if it has any attributes.
    // if it does, construct a tuple type value of the form
    // (0: signal type, 1: (attributes)) and add it to the input types.
    // otherwise, only add the signal type to the input types.
    for (MappedArray<String, TypeValue>.Entry e : oldInputs.getSubtypes()) {
      if (e.getValue().equals(NilTypeValue.getInstance())) {
        continue;
      }
      String key = e.getKey();
      if (e.getValue() instanceof PortTypeValue) {
        PortTypeValue port = (PortTypeValue) e.getValue();
        // are there any attributes?
        if (port.getAttributes().isEmpty()) {
          // just add the signal type
          inputTypes.put(key, port.getSignalType());
        } else {
          // construct (signal type, attributes)
          TupleTypeValue attrsType = new TupleTypeValue(new MappedArray<>(port.getAttributes()));
          MappedArray<String, TypeValue> sigType = new MappedArray<>();
          sigType.put("0", port.getSignalType());
          sigType.put("1", attrsType);
          inputTypes.put(key, new TupleTypeValue(sigType));
        }
      } else {
        TypeValue attr = e.getValue();
        inputTypes.put(key, attr);
      }
    }

    // iterate over the old output types to build the new output type and the
    // remainder of the new input type.
    // all types encountered here should be Port types;
    // for each one, add the signal type to the output types,
    // and add the attributes, if any exist, as a tuple to the input types.
    for (MappedArray<String, TypeValue>.Entry e : oldOutputs.getSubtypes()) {
      if (e.getValue().equals(NilTypeValue.getInstance())) {
        continue;
      }
      String key = e.getKey();
      PortTypeValue port = (PortTypeValue) e.getValue();
      outputTypes.put(key, port.getSignalType());
      if (!(port.getAttributes().isEmpty())) {
        inputTypes.put(key, new TupleTypeValue(new MappedArray<>(port.getAttributes())));
      }
    }

    return new FunctionTypeValue(
        new TupleTypeValue(inputTypes), new TupleTypeValue(outputTypes));
  }

  @Override
  public void elaborate() throws Exception {
    if (node != null) {
      return;
    }
    log.debug("elaborating primitive node");

    MappedArray<String, PortTypeValue> portTypeMap = new MappedArray<>();
    MappedArray<String, TypeValue> attributesMap = new MappedArray<>();

    ExpressionVertex portTypeVertex = signatureEdge.getSource();
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

    // look for PortTypeValues in the function signature,
    // then extract the identifier-typevalue
    // pairs and add them to the port type map
    log.debug("extracting input port types");
    extractPortTypes(portType.getInputType(), portTypeMap);
    log.debug("extracting output port types");
    extractPortTypes(portType.getOutputType(), portTypeMap);

    // look for non-PortTypeValues in the input type only,
    // then extract the pairs and add to the attributes map
    log.debug("extracting attributes");
    extractAttributes(portType.getInputType(), attributesMap);

    this.node = new NodeTypeValue(MappedArray.toMap(attributesMap), MappedArray.toMap(portTypeMap));
    log.debug("constructed node type " + debugNodeType(node));
    this.instantiationSignature = constructInstantiationSignature(portType);
    log.debug("instantiation signature is "
        + instantiationSignature.toString());
  }

  private String debugNodeType(NodeTypeValue n) {
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    sb.append(" ports: [ ");
    for (Map.Entry<String, PortTypeValue> e : n.getPorts().entrySet()) {
      sb.append(e.getKey()).append(":")
        .append(e.getValue().getSignalType()).append(" ");
    }
    sb.append("]");
    sb.append(" attributes: [ ");
    for (Map.Entry<String, TypeValue> e : n.getAttributes().entrySet()) {
      sb.append(e.getKey()).append(":")
      .append(e.getValue()).append(" ");
    }
    sb.append("] ");
    sb.append("]");
    return sb.toString();
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
  public ExpressionVertex copy(ExpressionGraph g, Map<ExpressionEdge, ExpressionEdge> edgeMap) {
    Preconditions.checkArgument(edgeMap.containsKey(signatureEdge));
    return new PrimitiveNodeVertex(g, edgeMap.get(signatureEdge));
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
