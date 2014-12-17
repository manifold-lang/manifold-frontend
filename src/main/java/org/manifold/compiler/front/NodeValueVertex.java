package org.manifold.compiler.front;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.manifold.compiler.NodeTypeValue;
import org.manifold.compiler.NodeValue;
import org.manifold.compiler.PortTypeValue;
import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

public class NodeValueVertex extends ExpressionVertex {

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

  @Override
  public void elaborate() throws Exception {
    // prevent infinite recursive elaboration
    if (futureOutputPorts != null) {
      return;
    }
    // decompose signature into (input) -> (output)
    TupleTypeValue inputType = (TupleTypeValue) signature.getInputType();
    TupleTypeValue outputType = (TupleTypeValue) signature.getOutputType();
    // construct values for all future output ports
    // simultaneously build a collection of all input port names
    Set<String> inputPortNames = new HashSet<>();
    inputPortNames.addAll(nodeType.getPorts().keySet());
    Map<String, Value> futurePortMap = new HashMap<>();
    for (String outputPortName : outputType.getSubtypes().keySet()) {
      PortTypeValue outputPortType = nodeType.getPorts().get(outputPortName);
      FuturePortValue futurePort = new FuturePortValue(
          this, outputPortName, outputPortType);
      futurePortMap.put(outputPortName, futurePort);
      inputPortNames.remove(outputPortName);
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
      Value inputPortValue = input.entry(inputPortName);
      // if the port has attributes, this is a tuple
      // (0: FuturePortValue, 1: (attributes))
      // otherwise, this is just a FuturePortValue
      FuturePortValue futurePort;
      Map<String, Value> inputPortAttrs;
      if (inputPortType.getAttributes().isEmpty()) {
        futurePort = (FuturePortValue) inputPortValue;
        futureInputPorts.put(inputPortName, futurePort);
        inputPortAttrs = new HashMap<>(); // no attributes
      } else {
        TupleValue inputPortTuple = (TupleValue) inputPortValue;
        futurePort = (FuturePortValue) inputPortTuple.entry("0");
        TupleValue attributesValue = (TupleValue) inputPortTuple.entry("1");
        inputPortAttrs = attributesValue.getEntries();
      }
      futureInputPorts.put(inputPortName, futurePort);
      portAttrs.put(inputPortName, inputPortAttrs);
    }

    for (String attrName : nodeType.getAttributes().keySet()) {
      Value attrValue = input.entry(attrName);
      nodeAttrs.put(attrName, attrValue);
    }

    node = new NodeValue(nodeType, nodeAttrs, portAttrs);

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

}
