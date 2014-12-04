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

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.manifold.compiler.UndefinedBehaviourError;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class ExpressionGraph {

  private static Logger log = LogManager.getLogger("ExpressionGraph");

  private Map<VariableIdentifier, VariableReferenceVertex> variableVertices =
      new HashMap<>();
  public Map<VariableIdentifier, VariableReferenceVertex> getVariableVertices()
  {
    return ImmutableMap.copyOf(variableVertices);
  }
  public VariableReferenceVertex getVariableVertex(VariableIdentifier vID)
      throws VariableNotDefinedException {
    if (variableVertices.containsKey(vID)) {
      return variableVertices.get(vID);
    } else {
      throw new VariableNotDefinedException(vID);
    }
  }

  public void createVariableVertex(VariableIdentifier vID)
      throws MultipleDefinitionException {
    if (variableVertices.containsKey(vID)) {
      throw new MultipleDefinitionException(vID);
    } else {
      variableVertices.put(vID, new VariableReferenceVertex(vID));
    }
  }

  private List<ExpressionVertex> nonVariableVertices = new LinkedList<>();
  public List<ExpressionVertex> getNonVariableVertices() {
    return ImmutableList.copyOf(nonVariableVertices);
  }
  public void addNonVariableVertex(ExpressionVertex v) {
    nonVariableVertices.add(v);
  }

  public List<ExpressionVertex> getVertices() {
    List<ExpressionVertex> vertices = new LinkedList<>();
    vertices.addAll(getVariableVertices().values());
    vertices.addAll(getNonVariableVertices());
    return ImmutableList.copyOf(vertices);
  }

  private List<ExpressionEdge> edges = new ArrayList<>();
  public void addEdge(ExpressionEdge e) {
    // TODO assert that both endpoints are in the graph
    edges.add(e);
  }

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

  public ExpressionGraph() {
  }

  public void writeDOTFile(File file) throws IOException {
    FileWriter fw = new FileWriter(file);
    try (BufferedWriter writer = new BufferedWriter(fw)) {
      // write graph header and attributes
      writer.write("digraph G {");
      writer.newLine();
      // write all vertices
      Set<ExpressionVertex> visited = new HashSet<ExpressionVertex>();
      for (ExpressionVertex v : getVertices()) {
        // do not write any vertex more than once
        if (visited.contains(v)) {
          continue;
        }
        visited.add(v);
        v.writeToDOTFile(writer);
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
      // contract each target edge onto source and rename
      for (ExpressionEdge targetEdge : targetEdges) {
        targetEdge.setSource(source);
        targetEdge.setName(id.getName() + targetEdge.getName());
      }
      // delete the source edge
      edges.remove(sourceEdge);
      // delete the variable
      varIt.remove();
    }
  }


}
