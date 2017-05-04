package org.manifold.compiler.front;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.manifold.compiler.*;
import org.manifold.compiler.middle.Schematic;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;

public class Main implements Frontend {

  private static Logger log = LogManager.getLogger("DefaultFrontend");

  public Main() {}

  @Override
  public String getFrontendName() {
    return "default";
  }

  @Override
  public void registerArguments(Options options) {
    // TODO Auto-generated method stub

  }

  public static void elaborateFunctions(ExpressionGraph g) throws Exception {
    // Maintain a set of unelaborated function invocations and
    // iterate until this set is empty.
    Set<FunctionInvocationVertex> funcalls = new LinkedHashSet<>();
    // Add all function invocations initially present in the graph
    for (ExpressionVertex v : g.getNonVariableVertices()) {
      if (v instanceof FunctionInvocationVertex) {
        funcalls.add((FunctionInvocationVertex) v);
      }
    }
    int step = 1;
    // now proceed
    while (!(funcalls.isEmpty())) {
      // get next vertex
      Iterator<FunctionInvocationVertex> iterator = funcalls.iterator();
      FunctionInvocationVertex v = iterator.next();
      funcalls.remove(v);
      log.debug("elaborating function "
          + Integer.toString(System.identityHashCode(v)));
      v.elaborate();
      v.verify();
      log.debug("writing out expression graph at function elaboration step " +
          step);
      File elaboratedDot = new File("tmp.elaborated.step" + step + ".dot");
      g.writeDOTFile(elaboratedDot);
      step++;
      // TODO it would be more efficient for the vertex to tell us whether
      // any new function invocations were created during elaboration
      for (ExpressionVertex vNew : g.getNonVariableVertices()) {
        if (vNew instanceof FunctionInvocationVertex) {
          funcalls.add((FunctionInvocationVertex) vNew);
        }
      }
    }
  }

  public static void elaborateSchematicTypes(ExpressionGraph g, Schematic s)
      throws Exception {
    // look for all primitive node/port vertices in the expression graph;
    // for each one, find any variables to which
    // it is directly assigned, and use the non-namespaced identifier
    // as the type name in the schematic
    for (ExpressionVertex v : g.getNonVariableVertices()) {
      if (v instanceof PrimitivePortVertex ||
          v instanceof PrimitiveNodeVertex) {
        v.elaborate(); // usually redundant but always safe
        List<ExpressionEdge> outgoingEdges = g.getEdgesFromSource(v);
        for (ExpressionEdge e : outgoingEdges) {
          if (e.getTarget() instanceof VariableReferenceVertex) {
            VariableReferenceVertex id = (VariableReferenceVertex)
                e.getTarget();
            String typename = id.getIdentifier().getName();
            // now add to the correct schematic type list
            if (v instanceof PrimitivePortVertex) {
              log.debug("elaborated port type " + typename);
              PortTypeValue portType = (PortTypeValue) v.getValue();
              s.addPortType(typename, portType);
            } else if (v instanceof PrimitiveNodeVertex) {
              log.debug("elaborated node type " + typename);
              NodeTypeValue nodeType = (NodeTypeValue) v.getValue();
              s.addNodeType(typename, nodeType);
            }
          }
        } // for (e: outgoingEdges)
      } // if (v instanceof ...)
    }
  }

  public static void elaborateNodes(ExpressionGraph g, Schematic s)
      throws Exception {
    // In the first pass, elaborate every NodeValueVertex
    // in the expression graph. In the second pass, connect the inputs of
    // every NodeValueVertex, then add to the schematic
    // each node and each connection that was created this way.

    log.debug("elaborating nodes");

    List<NodeValueVertex> nodeVertices = new LinkedList<>();
    for (ExpressionVertex v : g.getNonVariableVertices()) {
      if (v instanceof NodeValueVertex) {
        nodeVertices.add((NodeValueVertex) v);
      }
    }

    // pass 1
    log.debug("pass 1");
    for (NodeValueVertex nv : nodeVertices) {
      log.debug("elaborating node "
          + Integer.toString(System.identityHashCode(nv)));
      nv.elaborate();
    }
    // pass 2
    log.debug("pass 2");
    List<ConnectionValue> connections = new LinkedList<>();
    List<NodeValue> nodes = new LinkedList<>();
    for (NodeValueVertex nv : nodeVertices) {
      log.debug("connecting node "
          + Integer.toString(System.identityHashCode(nv)));
      List<ConnectionValue> cs = nv.connect();
      if (cs.size() == 1) {
        log.debug("1 connection made");
      } else {
        log.debug(Integer.toString(cs.size()) + " connections made");
      }
      connections.addAll(cs);
      nodes.add(nv.getNodeValue());
    }
    // build schematic
    Integer nodeID = 1;
    for (NodeValue node : nodes) {
      String nodeName = "n" + nodeID.toString();
      s.addNode(nodeName, node);
      nodeID += 1;
    }
    Integer connectionID = 1;
    for (ConnectionValue conn : connections) {
      String connName = "c" + connectionID.toString();
      s.addConnection(connName, conn);
      connectionID += 1;
    }

  }

  public static void verifyVariables(ExpressionGraph g) throws Exception {
    // verify that variables are only assigned once and
    // iterate through all the variables in the graph and verify that
    // there aren't any type mismatches.
    log.debug("verifying variables");

    g.verifyVariablesSingleAssignment();

    Map<VariableIdentifier, VariableReferenceVertex> variables = g.getVariableVertices();
    for (Map.Entry<VariableIdentifier, VariableReferenceVertex> entry : variables.entrySet()) {
      VariableReferenceVertex vertex = entry.getValue();
      vertex.verify();
    }
  }

  @Override
  public Schematic invokeFrontend(CommandLine cmd) throws Exception {

    File inputFile = Paths.get(cmd.getArgs()[0]).toFile();
    if (!inputFile.exists()) {
      throw new FrontendBuildException(inputFile.getName() + " not found.");
    }

    ExpressionGraphParser parser = new ExpressionGraphParser();
    ExpressionGraph exprGraph = parser.parseFile(inputFile);

    verifyVariables(exprGraph);
    elaborateFunctions(exprGraph);
    log.debug("writing out expression graph after function elaboration");
    File elaboratedDot = new File(inputFile.getName() + ".elaborated.dot");
    exprGraph.writeDOTFile(elaboratedDot);

    Schematic schematic = new Schematic(inputFile.getName());

    elaborateSchematicTypes(exprGraph, schematic);
    elaborateNodes(exprGraph, schematic);

    return schematic;

  }
}
