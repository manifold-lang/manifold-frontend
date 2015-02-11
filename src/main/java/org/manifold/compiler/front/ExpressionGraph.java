package org.manifold.compiler.front;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.base.Preconditions;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ExpressionGraph {

  private static Logger log = LogManager.getLogger(ExpressionGraph.class);

  Set<ExpressionVertex> allVertices = new HashSet<>();

  private Map<VariableIdentifier, VariableReferenceVertex> variableVertices =
      new HashMap<>();
  public Map<VariableIdentifier, VariableReferenceVertex> getVariableVertices() {
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

  private List<ExpressionEdge> edges = new ArrayList<>();
  public void addEdge(ExpressionEdge e) {
    Preconditions.checkArgument(
        getVertices().contains(e.getSource())
            && (e.getTarget() == null || getVertices().contains(e.getTarget())),
        "Edge had unexpected vertices " + e.toString());
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

  /**
   * Adds the provided subgraph to this graph
   * Precondition: there are no variable id conflicts between subGraph and this graph. That is
   *               each variable identifier in the subgraph is generated WRT its scope.
   *
   * @param subGraph ExpressionGraph to be added (elaborated function)
   * @param inputEdge edge of this graph to connect to subgraph (function args)
   * @param inputVertex vertex in the subgraph that inputEdge should connect to (function args)
   * @param outputVertex vertex in the subgraph that outputEdges should connect from (function return)
   * @param outputEdges edges that outputVertex connect from (function return)
   */
  public void addSubExpressionGraph(ExpressionGraph subGraph,
                                    ExpressionEdge inputEdge, ExpressionVertex inputVertex,
                                    VariableReferenceVertex outputVertex, List<ExpressionEdge> outputEdges) {

    // Sanity checks
    // input/output vertices exist in subgraph
    Preconditions.checkArgument(subGraph.allVertices.containsAll(
        ImmutableList.of(inputVertex, outputVertex)));

    // edges exist in main graph
    // TODO(max) - edge check is kind of expensive (O(n) search through edges) maybe change to a set?
    Preconditions.checkArgument(this.edges.containsAll(
        ImmutableList.of(inputEdge, outputEdges)));

    // all output edges have common source
    Preconditions.checkArgument(outputEdges.stream()
        .map(ExpressionEdge::getSource)
        .distinct()
        .count() == 1);

    // subGraph is being thrown away after, so we can just add the vertices/edges directly
    subGraph.allVertices.forEach(this::addVertex);
    subGraph.edges.forEach(this::addEdge);

    // removing old vertices - kind of awkward, but they're guaranteed to be var references (right?)
    VariableReferenceVertex oldInputVertex = (VariableReferenceVertex) inputEdge.getTarget();
    VariableReferenceVertex oldOutputVertex = (VariableReferenceVertex) outputEdges.get(0).getSource();

    allVertices.remove(oldInputVertex);
    allVertices.remove(oldOutputVertex);
    variableVertices.remove(oldInputVertex.getId());
    variableVertices.remove(oldOutputVertex.getId());

    // set the edges
    inputEdge.setTarget(inputVertex);
    outputEdges.forEach(edge -> edge.setSource(outputVertex));
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
