package org.manifold.compiler.front;

import com.google.common.base.Preconditions;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.manifold.compiler.*;
import org.manifold.compiler.TypeMismatchException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

public class NodeValueVertex extends ExpressionVertex {

  private static Logger log = LogManager.getLogger("NodeValueVertex");

  private final NodeTypeValue nodeType;
  @Override
  public TypeValue getType() {
    return nodeType;
  }
  private NodeValue node = null;
  public NodeValue getNodeValue() {
    return node;
  }

  // Return a tuple of "port futures" that allow ports to be referenced
  // before their associated node has been created.
  private TupleValue futureOutputPorts = null;
  @Override
  public Value getValue() {
    return futureOutputPorts;
  }

  private final FunctionTypeValue signature;

  private ExpressionEdge inputEdge;
  private Map<String, FuturePortValue> futureInputPorts = new HashMap<>();

  public NodeValueVertex(ExpressionGraph exprGraph, NodeTypeValue nodeType,
      FunctionTypeValue signature, ExpressionEdge inputEdge) {
    super(exprGraph);
    this.nodeType = nodeType;
    this.signature = signature;
    this.inputEdge = inputEdge;
    this.inputEdge.setTarget(this);
    this.inputEdge.setName("input");
  }

  private FuturePortValue unwrapPort(Value inputPortValue) {
    if (inputPortValue instanceof TupleValue) {
      TupleValue inputPortTuple = (TupleValue) inputPortValue;
      if (inputPortTuple.getSize() == 1) {
        // get "first" entry
        Iterator<Value> it = inputPortTuple.getEntries().values().iterator();
        // possibly wrapped again?
        return unwrapPort(it.next());
      } else {
        throw new UndefinedBehaviourError("cannot unwrap");
      }
    } else if (inputPortValue instanceof FuturePortValue) {
      return (FuturePortValue) inputPortValue;
    } else {
      throw new UndefinedBehaviourError("value not a port or 1-tuple of ports");
    }
  }

  @Override
  public void elaborate() throws Exception {
    // prevent infinite recursive elaboration
    if (futureOutputPorts != null) {
      return;
    }
    log.debug("type signature is " + signature.toString());
    // decompose signature into (input) -> (output)
    TupleTypeValue inputType = (TupleTypeValue) signature.getInputType();
    TupleTypeValue outputType = (TupleTypeValue) signature.getOutputType();
    // construct values for all future output ports
    // simultaneously build a collection of all input port names
    Set<String> inputPortNames = new HashSet<>();
    Set<String> outputPortNames = new HashSet<>();
    inputPortNames.addAll(nodeType.getPorts().keySet());
    MappedArray<String, Value> futurePortMap = new MappedArray<>();

    for (MappedArray<String, TypeValue>.Entry typeEntry : outputType.getSubtypes()) {
      String outputPortName = typeEntry.getKey();
      PortTypeValue outputPortType = nodeType.getPorts().get(outputPortName);
      FuturePortValue futurePort = new FuturePortValue(
          this, outputPortName, outputPortType);
      futurePortMap.put(outputPortName, futurePort);
      inputPortNames.remove(outputPortName);
      outputPortNames.add(outputPortName);
    }
    futureOutputPorts = new TupleValue(outputType, futurePortMap);

    inputEdge.getSource().elaborate();
    Value inputValue = inputEdge.getSource().getValue();
    TupleValue input = (TupleValue) inputValue;

    // now we have enough information to disassemble `input`
    // into attributes and (future) input ports

    Map<String, Value> nodeAttrs = new HashMap<>();
    Map<String, Map<String, Value>> portAttrs = new HashMap<>();

    for (String inputPortName : inputPortNames) {
      PortTypeValue inputPortType = nodeType.getPorts().get(inputPortName);
      Value inputPortValue = input.getEntry(inputPortName);
      // if the port has attributes, this is a tuple
      // (0: FuturePortValue, 1: (attributes))
      // otherwise, this is just a FuturePortValue
      FuturePortValue futurePort;
      Map<String, Value> inputPortAttrs;
      if (inputPortType.getAttributes().isEmpty()) {
        // in the case where a single output port is directly used as input,
        // this is a tuple of size 1 that needs to be "unwrapped" first
        futurePort = unwrapPort(inputPortValue);
        futureInputPorts.put(inputPortName, futurePort);
        inputPortAttrs = new HashMap<>(); // no attributes
      } else {
        TupleValue inputPortTuple = (TupleValue) inputPortValue;
        // same story here about unwrapping the port
        futurePort = unwrapPort(inputPortTuple.getEntry(0));
        TupleValue attributesValue = (TupleValue) inputPortTuple.getEntry(1);
        // TODO this can probably be done better, it assumes that all tuple values are named
        inputPortAttrs = MappedArray.toMap(attributesValue.getEntries());
      }
      futureInputPorts.put(inputPortName, futurePort);
      portAttrs.put(inputPortName, inputPortAttrs);
    }

    // we must also get output port attributes, which appear as function inputs
    for (String outputPortName : outputPortNames) {
      PortTypeValue outputPortType = nodeType.getPorts().get(outputPortName);
      Map<String, Value> outputPortAttrs;
      if (outputPortType.getAttributes().isEmpty()) {
        outputPortAttrs = new HashMap<>(); // no attributes
      } else {
        TupleValue attributesValue = (TupleValue) input.getEntry(outputPortName);
        outputPortAttrs = MappedArray.toMap(attributesValue.getEntries());
      }
      portAttrs.put(outputPortName, outputPortAttrs);
    }

    for (String attrName : nodeType.getAttributes().keySet()) {
      Value attrValue = input.getEntry(attrName);

      // This is the first time we can connect the type of the parameters to the infer value.
      // So we get the type from the signature and replace the empty inferred type with one containing
      // the correct type
      if (attrValue instanceof InferredValue) {
        InferredValue v = (InferredValue) attrValue;
        if (v.getInferredType() == null) {
          TypeValue tv = ((TupleTypeValue) signature.getInputType()).getEntry(attrName);
          attrValue = new InferredValue(new InferredTypeValue(tv));
        }
      }

      nodeAttrs.put(attrName, attrValue);
    }

    node = new NodeValue(nodeType, nodeAttrs, portAttrs);

  }

  // Connect all input ports to their sources and return all connections formed.
  public List<ConnectionValue> connect()
      throws UndeclaredIdentifierException, UndeclaredAttributeException,
      InvalidAttributeException, TypeMismatchException {
    List<ConnectionValue> connections = new LinkedList<>();
    for (Map.Entry<String, FuturePortValue> e : futureInputPorts.entrySet()) {
      String inputPortName = e.getKey();
      FuturePortValue source = e.getValue();
      PortValue sourcePort = source.getPort();
      PortValue targetPort = node.getPort(inputPortName);
      // TODO connection attributes; for now we assume none
      Map<String, Value> attrs = new HashMap<>();
      ConnectionValue conn = new ConnectionValue(sourcePort, targetPort, attrs);
      connections.add(conn);
    }
    return connections;
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

  @Override
  public String toString() {
    if (node == null) {
      return "node (not elaborated)";
    } else {
      return "node (" + node.toString() + ")";
    }
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
    Preconditions.checkArgument(edgeMap.containsKey(inputEdge));
    return new NodeValueVertex(g, nodeType, signature, edgeMap.get(inputEdge));
  }
}
