package org.manifold.compiler.front;

import org.junit.Before;
import org.junit.Test;
import org.manifold.compiler.BooleanValue;

public class TestExpressionGraph {
  ExpressionGraph expressionGraph;

  @Before
  public void setup() {
    expressionGraph = new ExpressionGraph();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddEdge_sourceNotInGraph_exceptionThrown() {
    ExpressionVertex from = new ConstantValueVertex(expressionGraph, BooleanValue.getInstance(true));
    ExpressionVertex to = new ConstantValueVertex(expressionGraph, BooleanValue.getInstance(false));
    ExpressionEdge edgeOfTomCruise = new ExpressionEdge(from, to);
    expressionGraph.addEdge(edgeOfTomCruise);
  }

  @Test
  public void testAddEdge_targetNull_exceptionThrown() {
    ExpressionVertex from = new ConstantValueVertex(expressionGraph, BooleanValue.getInstance(true));
    ExpressionEdge edgeOfTomCruise = new ExpressionEdge(from, null);
    expressionGraph.addVertex(from);
    expressionGraph.addEdge(edgeOfTomCruise);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddEdge_targetNotInGraph_exceptionThrown() {
    ExpressionVertex from = new ConstantValueVertex(expressionGraph, BooleanValue.getInstance(true));
    ExpressionVertex to = new ConstantValueVertex(expressionGraph, BooleanValue.getInstance(false));
    ExpressionEdge edgeOfTomCruise = new ExpressionEdge(from, to);
    expressionGraph.addVertex(from);
    expressionGraph.addEdge(edgeOfTomCruise);
  }
}
