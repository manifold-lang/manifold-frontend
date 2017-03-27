package org.manifold.compiler.front;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.antlr.v4.runtime.misc.Nullable;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.manifold.compiler.UndefinedBehaviourError;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExpressionGraph {

  private static Logger log = LogManager.getLogger(ExpressionGraph.class);

  Set<ExpressionVertex> allVertices = new HashSet<>();

  private Map<VariableIdentifier, VariableReferenceVertex> variableVertices =
      new HashMap<>();
  public Map<VariableIdentifier, VariableReferenceVertex> getVariableVertices() {
    return ImmutableMap.copyOf(variableVertices);
  }

  /**
   * Copy everything from subGraph into this graph
   * @param subGraph the graph to copy
   */
  public void addSubGraph(ExpressionGraph subGraph) {
    addSubGraph(subGraph, null);
  }

  /**
   * Copy all vertices from subGraph into this graph.
   * @param subGraph the graph to copy
   * @param namespace The namespace for any exported variables. If this is null all variables will be exported under
   *                  the same name.
   */
  public void addSubGraph(ExpressionGraph subGraph, @Nullable NamespaceIdentifier namespace) {
    // map of subgraph edge -> new edge to be inserted
    Map<ExpressionEdge, ExpressionEdge> exprEdgeMap = new HashMap<>();
    subGraph.getEdges().forEach(e -> {
      // replace these with the correct vertices later
      ExpressionEdge newEdge = new ExpressionEdge(null, null);
      exprEdgeMap.put(e, newEdge);
    });

    // map of subgraph vertex -> new vertex
    Map<ExpressionVertex, ExpressionVertex> exprVertexMap = new HashMap<>();
    List<String> oldNs = namespace == null ? new ArrayList<>() : namespace.getName();

    // do not add the input/output vertices since they are being replaced by the main graph's vertices
    subGraph.getVertices().stream()
        .forEach(v -> {
          ExpressionVertex newVertex;
          if (v instanceof VariableReferenceVertex) {
            VariableReferenceVertex var = (VariableReferenceVertex) v;
            // Allow for ignoring the state of import, this is the case when this function
            // is used to copy the body of a FunctionValueVertex
            if (namespace == null || var.getExported()) {
              List<String> newNs =
                  Stream.concat(
                      oldNs.stream(),
                      var.getId().getNamespaceIdentifier().getName().stream())
                      .collect(Collectors.toList());
              VariableIdentifier ref = new VariableIdentifier(new NamespaceIdentifier(newNs), var.getId().getName());
              // If this graph uses an exported variable then that namespaced variable will already be declared in
              // the graph. So we just need to link it up
              // If a variable is used in the importing module then the namespaced identifier will already exist
              if (!containsVariable(ref)) {
                try {
                  addVertex(ref);
                } catch (MultipleDefinitionException e) {
                  // unreachable
                }
              }
              newVertex = this.getVariableVertex(ref);
            } else {
              newVertex = new VariableReferenceVertex(this, var.getId());
              // Uses the ExpressionVertex overload which will add this node to the non-variable vertices,
              // instead of variable
              this.addVertex(newVertex);
            }
          } else {
            newVertex = v.copy(this, exprEdgeMap);
            this.addVertex(newVertex);
          }
          exprVertexMap.put(v, newVertex);
        });

    // each edge in subgraph -> edge in main graph should refer to the same source/target
    subGraph.getEdges().forEach(edge -> {
      if (edge.getSource() != null) {
        exprEdgeMap.get(edge).setSource(exprVertexMap.get(edge.getSource()));
      }
      if (edge.getTarget() != null) {
        exprEdgeMap.get(edge).setTarget(exprVertexMap.get(edge.getTarget()));
      }
    });

    this.edges.addAll(exprEdgeMap.values());
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
    addVertex(new VariableReferenceVertex(this, vID));
  }

  public void addVertex(VariableReferenceVertex v)
      throws MultipleDefinitionException {
    VariableIdentifier vID = v.getId();
    if (variableVertices.containsKey(vID)) {
      throw new MultipleDefinitionException(vID);
    } else {
      variableVertices.put(vID, v);
      allVertices.add(v);
    }
  }

  private List<ExpressionVertex> nonVariableVertices = new LinkedList<>();
  public List<ExpressionVertex> getNonVariableVertices() {
    return ImmutableList.copyOf(nonVariableVertices);
  }
  public void addVertex(ExpressionVertex v) {
    if (v == null) {
      throw new UndefinedBehaviourError("attempt to add null vertex to graph");
    }
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
    allVertices.remove(v);
  }

  private List<ExpressionEdge> edges = new ArrayList<>();
  public void addEdge(ExpressionEdge e) {
    Preconditions.checkArgument(
        (e.getSource() == null || getVertices().contains(e.getSource()))
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
   * Vertices and edges from the subgraph are copied into the mainGraph
   *
   * @param subGraph  ExpressionGraph to be added (elaborated function)
   * @param mainGraphInput Variable -> Function call edge in main graph
   * @param subGraphInput Entry vertex in subGraph
   * @param mainGraphOutput Function return -> Variable edge in main graph
   * @param subGraphOutput Exit vertex in subGraph
   */
  public void addFunctionExpressionGraph(ExpressionGraph subGraph,
                                         ExpressionEdge mainGraphInput, TupleValueVertex subGraphInput,
                                         ExpressionEdge mainGraphOutput, TupleValueVertex subGraphOutput,
                                         Map<VariableReferenceVertex, VariableReferenceVertex> variableRenamingMap) {

    // Sanity checks
    // input/output vertices exist in mainGraph and subGraph
    Preconditions.checkArgument(subGraph.getVertices().containsAll(
        ImmutableList.of(subGraphInput, subGraphOutput)));
    Preconditions.checkArgument(this.edges.containsAll(
        ImmutableList.of(mainGraphInput, mainGraphOutput)));

    // input edge and output edge should be connected through the same node (the function invocation)
    Preconditions.checkArgument(mainGraphInput.getTarget() == mainGraphOutput.getSource());

    // first, remove the function invocation vertex & the edges dealing with it
    this.allVertices.remove(mainGraphInput.getTarget());
    this.edges.removeAll(ImmutableList.of(mainGraphInput, mainGraphOutput));

    // map of subgraph edge -> new edge to be inserted
    Map<ExpressionEdge, ExpressionEdge> exprEdgeMap = new HashMap<>();
    subGraph.getEdges().forEach(e -> {
      // replace these with the correct vertices later
      ExpressionEdge newEdge = new ExpressionEdge(null, null);
      exprEdgeMap.put(e, newEdge);
    });

    // map of subgraph vertex -> new vertex
    Map<ExpressionVertex, ExpressionVertex> exprVertexMap = new HashMap<>();

    // do not add the input/output vertices since they are being replaced by the main graph's vertices
    subGraph.getVertices().stream()
        .filter(vertex -> vertex != subGraphInput)
        .forEach(v -> {
          ExpressionVertex newVertex;
          if (v instanceof VariableReferenceVertex) {
            // special case; handle renaming here
            if (variableRenamingMap.containsKey(v)) {
              newVertex = variableRenamingMap.get(v);
            } else {
              VariableIdentifier ref = ((VariableReferenceVertex) v).getId();
              try {
                this.addVertex(ref);
                newVertex = this.getVariableVertex(ref);
              } catch (MultipleDefinitionException | VariableNotDefinedException e) {
                throw Throwables.propagate(e);
              }
            }
          } else {
            newVertex = v.copy(this, exprEdgeMap);
            this.addVertex(newVertex);
          }
          exprVertexMap.put(v, newVertex);
        });

    // Replace the function input vertex with the vertices from the main graph
    ExpressionVertex inputVertex = mainGraphInput.getSource();
    exprVertexMap.put(subGraphInput, inputVertex);

    // Connect the function output vertex to the main graph
    ExpressionVertex outputVertex = exprVertexMap.get(subGraphOutput);
    if (subGraphOutput.getValueEdges().size() == 1) {
      // Remove the output tuple from the expression graph
      this.allVertices.remove(outputVertex);
      this.nonVariableVertices.remove(outputVertex);

      // Find the single value in the tuple and assign connect it to the output
      ExpressionEdge functionReturnEdge = subGraphOutput.getValueEdges().get(0);
      subGraph.removeEdge(functionReturnEdge);

      outputVertex = exprVertexMap.get(functionReturnEdge.getSource());
    }
    ExpressionEdge outputEdge = new ExpressionEdge(outputVertex, mainGraphOutput.getTarget());
    this.edges.add(outputEdge);

    // each edge in subgraph -> edge in main graph should refer to the same source/target
    subGraph.getEdges().forEach(edge -> {
      if (edge.getSource() != null) {
        exprEdgeMap.get(edge).setSource(exprVertexMap.get(edge.getSource()));
      }
      if (edge.getTarget() != null) {
        exprEdgeMap.get(edge).setTarget(exprVertexMap.get(edge.getTarget()));
      }
    });

    this.edges.addAll(exprEdgeMap.values());
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
        log.error(error.toString());
        errors.add(((VariableReferenceVertex) vertex).getId() + " is defined multiple times");
      }
    });

    if (!errors.isEmpty()) {
      throw new FrontendBuildException(errors.toString());
    }
  }

}
