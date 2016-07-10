package org.manifold.compiler.front;

import org.manifold.compiler.TypeValue;
import org.manifold.compiler.UndefinedBehaviourError;
import org.manifold.compiler.Value;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ImportVertex extends ExpressionVertex {

  private File importedFile;
  private NamespaceIdentifier namespace;
  private TypeValue type;
  private Value value;

  public ImportVertex(ExpressionGraph exprGraph, File importedFile) {
    super(exprGraph);
    this.importedFile = importedFile;
  }

  @Override
  public TypeValue getType() {
    return this.type;
  }

  @Override
  public Value getValue() {
    return this.value;
  }

  @Override
  public void elaborate() throws Exception {
    if (namespace != null) {
      return;
    }

    ExpressionGraphParser parser = new ExpressionGraphParser();
    ExpressionGraph importedGraph;
    try {
      importedGraph = parser.parseFile(importedFile);
    } catch (FrontendBuildException e) {
      throw new FrontendBuildException(
          "Could not parse " + importedFile.getName() + "\n" +
              e.getMessage());
    }

    ExpressionGraph exprGraph = getExpressionGraph();
    List<ExpressionEdge> targets = exprGraph.getEdgesFromSource(this);
    if (targets.size() != 1) {
      throw new UndefinedBehaviourError("Multiple edges from import " + this.toString());
    }
    ExpressionVertex target = targets.get(0).getTarget();
    if (!(target instanceof VariableReferenceVertex)) {
      throw new UndefinedBehaviourError("Import " + this.toString() + " must be assigned to a variable");
    }
    namespace = new NamespaceIdentifier(((VariableReferenceVertex) target).getId().getName());

    exprGraph.addSubGraph(importedGraph, namespace);
    this.value = new ImportValue(getExpressionGraph(), namespace);
    this.type = this.value.getType();
  }

  @Override
  public void verify() throws Exception {
    return;
  }

  @Override
  public boolean isElaborationtimeKnowable() {
    return false;
  }

  @Override
  public boolean isRuntimeKnowable() {
    return false;
  }

  @Override
  public void writeToDOTFile(BufferedWriter writer) throws IOException {
    return;
  }

  @Override
  public ExpressionVertex copy(ExpressionGraph g, Map<ExpressionEdge, ExpressionEdge> edgeMap) {
    ImportVertex v = new ImportVertex(g, importedFile);
    v.namespace = namespace;
    return v;
  }
}
