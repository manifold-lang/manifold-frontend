package org.manifold.compiler.front;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class ExpressionGraph {

  private static Logger log = LogManager.getLogger(ExpressionGraph.class);

  Set<ExpressionVertex> allVertices = new HashSet<>();

  private Map<VariableIdentifier, VariableReferenceVertex> variableVertices =
      new HashMap<>();
  public Map<VariableIdentifier, VariableReferenceVertex> getVariableVertices() {
    return ImmutableMap.copyOf(variableVertices);
  }
  
  public boolean containsVariable(VariableIdentifier vID) {
    return variableVertices.containsKey(vID);
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
      VariableReferenceVertex v = new VariableReferenceVertex(this, vID);
      variableVertices.put(vID, v);
      allVertices.add(v);
    }
  }

  private List<ExpressionVertex> nonVariableVertices = new LinkedList<>();
  public List<ExpressionVertex> getNonVariableVertices() {
    return ImmutableList.copyOf(nonVariableVertices);
  }
  public void addVertex(ExpressionVertex v) {
    nonVariableVertices.add(v);
    allVertices.add(v);
  }

  public Collection<ExpressionVertex> getVertices() {
    return allVertices;
  }

  public void removeVertex(ExpressionVertex v) {
    // remove all occurrences from map variableVertices
    while (variableVertices.values().remove(v)) { }
    // simple removal from nonVariableVertices
    nonVariableVertices.remove(v);
  }
  
  private List<ExpressionEdge> edges = new ArrayList<>();
  public void addEdge(ExpressionEdge e) {
    Preconditions.checkArgument(
        getVertices().contains(e.getSource())
            && (e.getTarget() == null || getVertices().contains(e.getTarget())),
        "Edge had unexpected vertices " + e.toString());
    edges.add(e);
  }
  public void removeEdge(ExpressionEdge e) {
    edges.remove(e);
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

  /**
   * Adds the provided subgraph to this graph
   * Precondition: there are no variable id conflicts between subGraph and this graph. That is
   *               each variable identifier in the subgraph is generated WRT its scope.
   *
   * In the final graph, subGraphInput/subGraphOutput will no longer exist, and the edges from/to them
   * should be from mainGraphInput/mainGraphOutput respectively.
   *
   * The edge from mainGraphInput to the function vertex & the edge from the function vertex to mainGraphOutput
   * are not removed (since 1 vertex can potentially point to multiple functions), so they should be deleted by
   * the caller.
   *
   * @param subGraph  ExpressionGraph to be added (elaborated function)
   * @param mainGraphInput Variable -> Function call edge in main graph
   * @param subGraphInput Entry vertex in subGraph
   * @param mainGraphOutput Function return -> Variable edge in main graph
   * @param subGraphOutput Exit vertex in subGraph
   */
  public void addSubExpressionGraph(ExpressionGraph subGraph,
                                    ExpressionEdge mainGraphInput, ExpressionVertex subGraphInput,
                                    ExpressionEdge mainGraphOutput, ExpressionVertex subGraphOutput) {

    // Sanity checks
    // input/output vertices exist in mainGraph and subGraph
    Preconditions.checkArgument(subGraph.allVertices.containsAll(
        ImmutableList.of(subGraphInput, subGraphOutput)));
    Preconditions.checkArgument(this.edges.containsAll(
        ImmutableList.of(mainGraphInput, mainGraphOutput)));

    // input edge and output edge should be connected through the same node (the function invocation)
    Preconditions.checkArgument(mainGraphInput.getTarget() == mainGraphOutput.getSource());

    // first, remove the function invocation vertex & the edges dealing with it
    this.allVertices.remove(mainGraphInput.getTarget());
    this.edges.removeAll(ImmutableList.of(mainGraphInput, mainGraphOutput));

    ExpressionVertex inputVertex = mainGraphInput.getSource();
    ExpressionVertex outputVertex = mainGraphOutput.getTarget();

    // subGraph is being thrown away after, so we can just add the vertices/edges directly
    // do not add the input/output vertices since they are being replaced by the main graph's vertices
    subGraph.allVertices.stream()
        .filter(vertex -> (vertex != subGraphInput && vertex != subGraphOutput))
        .forEach(this::addVertex);

    // subGraphOutput's in edges -> switch source to mainGraphOutput
    subGraph.getEdgesToTarget(subGraphOutput).forEach(edge -> edge.setTarget(outputVertex));

    // subGraphInput's out edges -> switch source to mainGraphInput
    subGraph.getEdgesFromSource(subGraphInput).forEach(edge -> edge.setSource(inputVertex));

    subGraph.edges.forEach(this::addEdge);
  }


  public void writeDOTFile(File file) throws IOException {
    FileWriter fw = new FileWriter(file);
    try (BufferedWriter writer = new BufferedWriter(fw)) {
      // write graph header and attributes
      writer.write("digraph \"" + file.getName() + "\" {");
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
    fw.close();
  }

  public void verifyVariablesSingleAssignment() {
    Map<ExpressionVertex, List<ExpressionEdge>> inboundEdges = new HashMap<>();
    getEdges().forEach(exprEdge -> {
        ExpressionVertex v = exprEdge.getTarget();
        if (v instanceof VariableReferenceVertex) {
          inboundEdges.putIfAbsent(v, new ArrayList<>());
          inboundEdges.get(v).add(exprEdge);
        }
      });

    List<String> errors = new ArrayList<>();
    inboundEdges.forEach((vertex, edges) -> {
        if (edges.size() != 1) {
          StringBuilder error = new StringBuilder();
          error.append(String.format("Vertex %s has %d incoming edges:",
              vertex.toString(), edges.size()));
          edges.forEach(edge -> error.append(" {")
              .append(edge.toString())
              .append("}"));
          errors.add(error.toString());
        }
      });

    if (!errors.isEmpty()) {
      throw new RuntimeException(errors.toString());
    }
  }
  
}
