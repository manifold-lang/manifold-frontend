package org.manifold.compiler.front;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.manifold.compiler.ArrayTypeValue;
import org.manifold.compiler.ArrayValue;
import org.manifold.compiler.BooleanTypeValue;
import org.manifold.compiler.BooleanValue;
import org.manifold.compiler.ConnectionType;
import org.manifold.compiler.ConnectionValue;
import org.manifold.compiler.ConstraintType;
import org.manifold.compiler.ConstraintValue;
import org.manifold.compiler.IntegerTypeValue;
import org.manifold.compiler.IntegerValue;
import org.manifold.compiler.InvalidAttributeException;
import org.manifold.compiler.MultipleAssignmentException;
import org.manifold.compiler.NilTypeValue;
import org.manifold.compiler.NodeTypeValue;
import org.manifold.compiler.NodeValue;
import org.manifold.compiler.PortTypeValue;
import org.manifold.compiler.PortValue;
import org.manifold.compiler.RealTypeValue;
import org.manifold.compiler.RealValue;
import org.manifold.compiler.StringTypeValue;
import org.manifold.compiler.StringValue;
import org.manifold.compiler.TypeMismatchException;
import org.manifold.compiler.TypeTypeValue;
import org.manifold.compiler.TypeValue;
import org.manifold.compiler.UndeclaredAttributeException;
import org.manifold.compiler.UndeclaredIdentifierException;
import org.manifold.compiler.UndefinedBehaviourError;
import org.manifold.compiler.Value;
import org.manifold.compiler.middle.Schematic;
import org.manifold.compiler.middle.SchematicException;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class ExpressionGraph
    implements ExpressionVisitor, FrontendValueVisitor {

  private List<PrimitiveFunctionVertex> primitiveFunctionVertices =
      new ArrayList<>();
  public List<PrimitiveFunctionVertex> getPrimitiveFunctionVertices() {
    return ImmutableList.copyOf(primitiveFunctionVertices);
  }

  private Map<VariableIdentifier, VariableReferenceVertex> variableVertices =
      new HashMap<>();
  public Map<VariableIdentifier, VariableReferenceVertex> getVariableVertices()
  {
    return ImmutableMap.copyOf(variableVertices);
  }

  private List<ExpressionEdge> edges = new ArrayList<>();

  public List<ExpressionEdge> getEdgesFromSource(ExpressionVertex v) {
    List<ExpressionEdge> edgesFrom = new LinkedList<>();
    for (ExpressionEdge e : edges) {
      if (v.equals(e.getSource())) {
        edgesFrom.add(e);
      }
    }
    return edgesFrom;
  }

  public List<ExpressionEdge> getEdgesToTarget(ExpressionVertex v) {
    List<ExpressionEdge> edgesTo = new LinkedList<>();
    for (ExpressionEdge e : edges) {
      if (v.equals(e.getTarget())) {
        edgesTo.add(e);
      }
    }
    return edgesTo;
  }

  public List<String> getPrintableEdges() {
    List<String> edgeList = new LinkedList<>();
    for (ExpressionEdge e : edges) {
      edgeList.add(e.toString());
    }
    return edgeList;
  }

  private Scope scope;

  public ExpressionGraph(Scope scope) {
    this.scope = scope;
  }

  public void buildFrom(List<Expression> expressions) {
    for (Expression e : expressions) {
      e.accept(this);
    }
  }

  private void writePrimitiveFunctionVertex(
      BufferedWriter writer, PrimitiveFunctionVertex v)
      throws IOException {
    String objectID = Integer.toString(System.identityHashCode(v));
    String label = v.toString();
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

  private void writeVariableVertex(
      BufferedWriter writer, VariableReferenceVertex v) throws IOException {
    String objectID = Integer.toString(System.identityHashCode(v));
    String label = v.toString();
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

  private void writeTupleValueVertex(
      BufferedWriter writer, TupleValueVertex v) throws IOException {
    String objectID = Integer.toString(System.identityHashCode(v));
    String label = v.toString();
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

  public void writeDOTFile(File file) throws IOException {
    FileWriter fw = new FileWriter(file);
    try (BufferedWriter writer = new BufferedWriter(fw)) {
      // write graph header and attributes
      writer.write("digraph G {");
      writer.newLine();
      // write all vertices
      Set<ExpressionVertex> visited = new HashSet<ExpressionVertex>();
      for (ExpressionEdge e : edges) {
        ExpressionVertex endpoints[] = {e.getSource(), e.getTarget()};
        for (ExpressionVertex v : endpoints) {
          // do not write any vertex more than once
          if (visited.contains(v)) {
            continue;
          }
          visited.add(v);
          // TODO(murphy) refactor to an abstract method on ExpressionVertex
          if (v instanceof PrimitiveFunctionVertex) {
            writePrimitiveFunctionVertex(writer, (PrimitiveFunctionVertex) v);
          } else if (v instanceof VariableReferenceVertex) {
            writeVariableVertex(writer, (VariableReferenceVertex) v);
          } else if (v instanceof TupleValueVertex) {
            writeTupleValueVertex(writer, (TupleValueVertex) v);
          } else {
            throw new UnsupportedOperationException(
                "unhandled expression vertex type '" + v.toString() + "'");
          }
        }
      }

      // write all edges
      for (ExpressionEdge e : edges) {
        String source = Integer.toString(System.identityHashCode(
            e.getSource()));
        String target = Integer.toString(System.identityHashCode(
            e.getTarget()));
        // for now
        writer.write(source + " -> " + target);
        if (!e.getName().equals("")) {
          // label it
          writer.write("[label=\"" + e.getName() + "\"]");
        }
        writer.write(";");
        writer.newLine();
      }
      // write graph footer
      writer.write("}");
      writer.newLine();
    }
  }

  // Remove every edge in the graph whose source or target is null.
  public void removeUnconnectedEdges() {
    Iterator<ExpressionEdge> edgeIt = edges.iterator();
    while (edgeIt.hasNext()) {
      ExpressionEdge edge = edgeIt.next();
      if (edge.getSource() == null || edge.getTarget() == null) {
        edgeIt.remove();
      } else if (edge.getSource() instanceof TupleValueVertex) {
        // all edges out of zero-length tuples are removed
        TupleValueVertex tuple = (TupleValueVertex) edge.getSource();
        if (tuple.getValue().getSize() == 0) {
          edgeIt.remove();
        }
      }
    }
  }

  // Optimize variables out of the design by finding each edge from a variable
  // to a target and setting the source of that edge to
  // the source of the variable.
  public void optimizeOutVariables() {
    Iterator<Map.Entry<VariableIdentifier, VariableReferenceVertex>> varIt =
        variableVertices.entrySet().iterator();
    while (varIt.hasNext()) {
      Map.Entry<VariableIdentifier, VariableReferenceVertex> entry =
          varIt.next();
      VariableIdentifier id = entry.getKey();
      VariableReferenceVertex vertex = entry.getValue();
      // find all edges for which this vertex is the source
      List<ExpressionEdge> targetEdges = getEdgesFromSource(vertex);
      // and find where the value of this variable comes from
      List<ExpressionEdge> sourceEdges = getEdgesToTarget(vertex);
      // there should only be one, ideally...
      if (sourceEdges.size() == 0) {
        throw new UndefinedBehaviourError(
            "unassigned variable '" + id.toString() + "'");
      } else if (sourceEdges.size() > 1) {
        throw new UndefinedBehaviourError(
            "multiply assigned variable '" + id.toString() + "'");
      }
      ExpressionEdge sourceEdge = sourceEdges.get(0);
      ExpressionVertex source = sourceEdge.getSource();
      // contract each target edge onto source and rename/retype
      for (ExpressionEdge targetEdge : targetEdges) {
        targetEdge.setSource(source);
        targetEdge.setType(sourceEdge.getType());
        targetEdge.setName(id.getName() + targetEdge.getName());
      }
      // delete the source edge
      edges.remove(sourceEdge);
      // delete the variable
      varIt.remove();
    }
  }

  public void elaboratePrimitives() throws SchematicException {
    Integer uuid = 0;
    for (PrimitiveFunctionVertex pFunc : primitiveFunctionVertices) {
      pFunc.elaborate();
      pFunc.setUniqueSuffix(uuid);
      uuid += 1;
    }
  }

  public List<String> getPrintableInstances() {
    List<String> instanceNames = new LinkedList<String>();
    for (PrimitiveFunctionVertex pFunc : primitiveFunctionVertices) {
      instanceNames.add(pFunc.getInstanceName() + ": "
          + pFunc.getNodeValue().toString());
    }
    return instanceNames;
  }

  private Map<String, ConnectionValue> connections = new HashMap<>();

  public void elaborateConnections(Schematic schematic)
      throws UndeclaredIdentifierException, UndeclaredAttributeException,
      InvalidAttributeException, TypeMismatchException {
    // TODO generalize connection type
    // TODO connection attributes
    ConnectionType connType = schematic.getConnectionType("digitalWire");
    PortTypeValue inputType = schematic.getPortType("digitalIn");
    PortTypeValue outputType = schematic.getPortType("digitalOut");
    Map<String, Value> noAttributes = new HashMap<>();

    Integer uuid = 0;

    for (ExpressionEdge edge : edges) {
      TypeValue edgeType = edge.getType();
      if (edgeType == null) {
        throw new UndefinedBehaviourError(
            "cannot elaborate edge " + edge.toString()
            + " because its type is null");
      }
      ExpressionVertex vSource = edge.getSource();
      ExpressionVertex vTarget = edge.getTarget();
      // if the target is not a primitive function, skip
      // (we'll do it in reverse)
      if (!(vTarget instanceof PrimitiveFunctionVertex)) {
        continue;
      }
      PrimitiveFunctionVertex target = (PrimitiveFunctionVertex) vTarget;
      if (edgeType instanceof BooleanTypeValue) {
        PrimitiveFunctionVertex source = (PrimitiveFunctionVertex) vSource;
        String sourcePortName = source.getNthPortOfType(outputType, 0);
        PortValue sourcePort = source.getNodeValue().getPort(sourcePortName);

        String targetPortName = target.getNthPortOfType(inputType, 0);
        PortValue targetPort = target.getNodeValue().getPort(targetPortName);

        ConnectionValue conn = new ConnectionValue(
            connType, sourcePort, targetPort, noAttributes);
        String connID = "n" + Integer.toString(uuid)
            + "_" + source.getInstanceName()
            + "_" + sourcePortName
            + "__" + target.getInstanceName()
            + "_" + targetPortName;
        uuid += 1;
        connections.put(connID, conn);
      } else if (edgeType instanceof TupleTypeValue
          && vSource instanceof PrimitiveFunctionVertex) {
        PrimitiveFunctionVertex source = (PrimitiveFunctionVertex) vSource;
        // check that #outputs = #inputs
        int nOutputs = source.getNumberOfPortsOfType(outputType);
        int nInputs = target.getNumberOfPortsOfType(inputType);
        if (!(nInputs == nOutputs)) {
          throw new UndefinedBehaviourError(
              "connected source "
              + source.getInstanceName()
              + " with " + nOutputs + " outputs"
              + " to target "
              + target.getInstanceName()
              + " with " + nInputs + " inputs");
        }
        // connect source 0 to target 0, etc.
        for (int i = 0; i < nInputs; ++i) {
          String sourcePortName = source.getNthPortOfType(outputType, i);
          PortValue sourcePort = source.getNodeValue().getPort(sourcePortName);
          String targetPortName = target.getNthPortOfType(inputType, i);
          PortValue targetPort = target.getNodeValue().getPort(targetPortName);
          ConnectionValue conn = new ConnectionValue(
              connType, sourcePort, targetPort, noAttributes);
          String connID = "n" + Integer.toString(uuid)
              + "_" + source.getInstanceName()
              + "_" + sourcePortName
              + "__" + target.getInstanceName()
              + "_" + targetPortName;
          uuid += 1;
          connections.put(connID, conn);
        }
      } else if (edgeType instanceof TupleTypeValue
          && vSource instanceof TupleValueVertex) {
        TupleValueVertex tuple = (TupleValueVertex) vSource;
        int nInputs = target.getNumberOfPortsOfType(inputType);
        if (!(nInputs == tuple.getValue().getSize())) {
          throw new UndefinedBehaviourError(
              "connected source "
              + tuple.toString()
              + " with " + tuple.getValue().getSize() + " outputs"
              + " to target "
              + target.getInstanceName()
              + " with " + nInputs + " inputs");
        }
        // in general this does not scale, but for
        // simple connections it is fine
        for (int i = 0; i < nInputs; ++i) {
          ExpressionEdge sourceEdge = tuple.getValueEdges().get(i);
          // again, this does not scale, but it is simple enough to be clear
          PrimitiveFunctionVertex source
              = (PrimitiveFunctionVertex) sourceEdge.getSource();

          String sourcePortName = source.getNthPortOfType(outputType, 0);
          PortValue sourcePort = source.getNodeValue().getPort(sourcePortName);
          String targetPortName = target.getNthPortOfType(inputType, i);
          PortValue targetPort = target.getNodeValue().getPort(targetPortName);
          ConnectionValue conn = new ConnectionValue(
              connType, sourcePort, targetPort, noAttributes);
          String connID = "n" + Integer.toString(uuid)
              + "_" + source.getInstanceName()
              + "_" + sourcePortName
              + "__" + target.getInstanceName()
              + "_" + targetPortName;
          uuid += 1;
          connections.put(connID, conn);
        }
      } else {
        throw new UndefinedBehaviourError(
            "cannot elaborate edge " + edge.toString()
            + " due to unhandled type");
      }
    }
  }

  public void writeSchematic(Schematic schematic)
      throws MultipleAssignmentException {
    // write all nodes
    for (PrimitiveFunctionVertex pFunc : primitiveFunctionVertices) {
      schematic.addNode(pFunc.getInstanceName(), pFunc.getNodeValue());
    }
    // write all connections
    for (Map.Entry<String, ConnectionValue> conn : connections.entrySet()) {
      schematic.addConnection(conn.getKey(), conn.getValue());
    }
  }

  // edge leading from the last expression we visited
  private ExpressionEdge lastSourceEdge = null;

  @Override
  public void visit(FunctionInvocationExpression functionInvocationExpression) {
    Expression funcExpr = functionInvocationExpression.getFunctionExpression();
    Expression inputExpr = functionInvocationExpression.getInputExpression();
    // first, visit the input
    inputExpr.accept(this);
    // get the edge coming from the input
    ExpressionEdge inputEdge = lastSourceEdge;
    // evaluate the expression and visit its value
    Value funcValue = funcExpr.getValue(scope);
    funcValue.accept(this);
    // get the edge coming from the function
    ExpressionEdge funcEdge = lastSourceEdge;
    // rewrite to connect input --inputEdge-> func
    ExpressionVertex func = funcEdge.getSource();
    inputEdge.setTarget(func);
    lastSourceEdge = funcEdge;
  }

  @Override
  public void visit(LiteralExpression literalExpression) {
    Value v = literalExpression.getValue(scope);
    v.accept(this);
  }

  @Override
  public void visit(VariableAssignmentExpression variableAssignmentExpression) {
    Expression lvalue = variableAssignmentExpression.getLvalueExpression();
    Expression rvalue = variableAssignmentExpression.getRvalueExpression();
    // first, visit the source
    rvalue.accept(this);
    // get the edge coming from the source
    ExpressionEdge sourceEdge = lastSourceEdge;
    // then visit the target
    lvalue.accept(this);
    // get the edge coming from the target (so that we can find the target)
    ExpressionEdge targetEdge = lastSourceEdge;
    // rewrite to connect source --sourceEdge-> target
    ExpressionVertex target = targetEdge.getSource();
    sourceEdge.setTarget(target);
    lastSourceEdge = targetEdge; // to support "a = b = c;"
  }

  @Override
  public void visit(VariableReferenceExpression variableReferenceExpression) {
    VariableIdentifier id = variableReferenceExpression.getIdentifier();
    VariableReferenceVertex source;
    if (!variableVertices.containsKey(id)) {
      source = new VariableReferenceVertex(id);
      variableVertices.put(id, source);
    } else {
      source = variableVertices.get(id);
    }
    ExpressionEdge edgeVariableOut = new ExpressionEdge(source, null,
        variableReferenceExpression.getType(scope));
    edges.add(edgeVariableOut);
    lastSourceEdge = edgeVariableOut;
  }

  @Override
  public void visit(ArrayTypeValue arrayTypeValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(PrimitiveFunctionValue pFunc) {
    PrimitiveFunctionVertex pVertex = new PrimitiveFunctionVertex(pFunc);
    primitiveFunctionVertices.add(pVertex);
    FunctionTypeValue fType = (FunctionTypeValue) pFunc.getType();
    ExpressionEdge edgeFunctionOut = new ExpressionEdge(pVertex, null,
        fType.getOutputType());
    edges.add(edgeFunctionOut);
    lastSourceEdge = edgeFunctionOut;
  }

  @Override
  public void visit(TupleValue tuple) {
    List<ExpressionEdge> valueEdges = new ArrayList<ExpressionEdge>();
    for (int i = 0; i < tuple.getSize(); ++i) {
      Expression e = tuple.entry(i);
      e.accept(this);
      ExpressionEdge valueEdge = lastSourceEdge;
      valueEdges.add(valueEdge);
    }
    TupleValueVertex tupleVertex = new TupleValueVertex(tuple, valueEdges);
    for (ExpressionEdge edgeTupleIn : valueEdges) {
      edgeTupleIn.setTarget(tupleVertex);
    }
    ExpressionEdge edgeTupleOut = new ExpressionEdge(tupleVertex, null,
        tuple.getType());
    edges.add(edgeTupleOut);
    lastSourceEdge = edgeTupleOut;
  }

  @Override
  public void visit(PrimitivePortTypeValue primitivePortTypeValue) {
    // TODO(murphy)
    throw new UnsupportedOperationException("not yet supported");
  }

  @Override
  public void visit(TupleTypeValue tupleTypeValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(FunctionValue functionValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(FunctionTypeValue functionTypeValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(EnumValue enumValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(EnumTypeValue enumTypeValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(TypeTypeValue typeTypeValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(StringValue stringValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(StringTypeValue stringTypeValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(PortValue portValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(PortTypeValue portTypeValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(NodeValue nodeValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(NodeTypeValue nodeTypeValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(NilTypeValue nilTypeValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(IntegerValue integerValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(ConstraintValue constraintValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(IntegerTypeValue integerTypeValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(ConstraintType constraintType) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(ConnectionValue connectionValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(ConnectionType connectionType) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(BooleanValue booleanValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(BooleanTypeValue booleanTypeValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(ArrayValue arrayValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(RealTypeValue arg0) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(RealValue arg0) {
    throw new UnsupportedOperationException("illegal value");
  }

}
