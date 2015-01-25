package org.manifold.compiler.front;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class ExpressionGraph {

  private static Logger log = LogManager.getLogger(ExpressionGraph.class);

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

  public void addVertex(VariableIdentifier vID)
      throws MultipleDefinitionException {
    if (variableVertices.containsKey(vID)) {
      throw new MultipleDefinitionException(vID);
    } else {
      variableVertices.put(vID, new VariableReferenceVertex(this, vID));
    }
  }

  private List<ExpressionVertex> nonVariableVertices = new LinkedList<>();
  public List<ExpressionVertex> getNonVariableVertices() {
    return ImmutableList.copyOf(nonVariableVertices);
  }
  public void addVertex(ExpressionVertex v) {
    nonVariableVertices.add(v);
  }

  public List<ExpressionVertex> getVertices() {
    List<ExpressionVertex> vertices = new LinkedList<>();
    vertices.addAll(getVariableVertices().values());
    vertices.addAll(getNonVariableVertices());
    return vertices;
  }

  private List<ExpressionEdge> edges = new ArrayList<>();
  public void addEdge(ExpressionEdge e) {
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

  public List<ExpressionEdge> getEdges() {
    return ImmutableList.copyOf(edges);
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

}
