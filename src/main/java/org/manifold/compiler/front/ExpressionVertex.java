package org.manifold.compiler.front;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

public abstract class ExpressionVertex {

  private final ExpressionGraph exprGraph;

  protected ExpressionGraph getExpressionGraph() {
    return this.exprGraph;
  }
  public ExpressionVertex (ExpressionGraph exprGraph) {
    this.exprGraph = exprGraph;
  }
  
  public abstract TypeValue getType();
  public abstract Value getValue();
  public abstract void elaborate() throws Exception;
  public abstract void verify() throws Exception;
  public abstract boolean isElaborationtimeKnowable();
  public abstract boolean isRuntimeKnowable();
  public abstract void writeToDOTFile(BufferedWriter writer) throws IOException;

  // creates a copy of this vertex but WRT the new expression graph
  public abstract ExpressionVertex copy(ExpressionGraph g, Map<ExpressionEdge, ExpressionEdge> edgeMap);
}
