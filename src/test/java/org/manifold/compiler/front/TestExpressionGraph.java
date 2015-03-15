package org.manifold.compiler.front;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.manifold.compiler.BooleanValue;
import org.manifold.compiler.StringTypeValue;
import org.manifold.compiler.StringValue;

import java.io.File;

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


  @Test
  public void testAddSubExpressionGraph_correctGraphAdded() throws Exception {
    // init main graph
    ExpressionGraph mainGraph = new ExpressionGraph();

    // input/output
    VariableIdentifier mainInput = new VariableIdentifier(ImmutableList.of("mainInput"));
    VariableIdentifier mainOutput = new VariableIdentifier(ImmutableList.of("mainOutput"));
    mainGraph.addVertex(mainInput);
    mainGraph.addVertex(mainOutput);

    ExpressionVertex mainInputVertex = mainGraph.getVariableVertex(mainInput);
    ExpressionVertex mainOutputVertex = mainGraph.getVariableVertex(mainOutput);

    // dummy vertex to remove
    ExpressionVertex dummyVertex = new ConstantValueVertex(mainGraph,
        new StringValue(StringTypeValue.getInstance(), "yo"));
    ExpressionEdge inputEdge = new ExpressionEdge(mainInputVertex, dummyVertex);
    ExpressionEdge outputEdge = new ExpressionEdge(dummyVertex, mainOutputVertex);
    mainGraph.addVertex(dummyVertex);
    mainGraph.addEdge(inputEdge);
    mainGraph.addEdge(outputEdge);

    // init subgraph
    ExpressionGraph subGraph = new ExpressionGraph();

    // input/output
    VariableIdentifier subInput = new VariableIdentifier(ImmutableList.of("subInput"));
    VariableIdentifier subOutput = new VariableIdentifier(ImmutableList.of("subOutput"));
    subGraph.addVertex(subInput);
    subGraph.addVertex(subOutput);

    ExpressionVertex subInputVertex = subGraph.getVariableVertex(subInput);
    ExpressionVertex subOutputVertex = subGraph.getVariableVertex(subOutput);

    // intermediate nodes
    ExpressionVertex intermediate1 = new ConstantValueVertex(subGraph, BooleanValue.getInstance(true));
    ExpressionVertex intermediate2 = new ConstantValueVertex(subGraph, BooleanValue.getInstance(false));
    subGraph.addVertex(intermediate1);
    subGraph.addVertex(intermediate2);

    subGraph.addEdge(new ExpressionEdge(subInputVertex, intermediate1));
    subGraph.addEdge(new ExpressionEdge(subInputVertex, intermediate2));
    subGraph.addEdge(new ExpressionEdge(intermediate1, subOutputVertex));
    subGraph.addEdge(new ExpressionEdge(intermediate2, subOutputVertex));

    mainGraph.writeDOTFile(new File("build/mainGraph.txt"));
    subGraph.writeDOTFile(new File("build/subGraph.txt"));

    mainGraph.addSubExpressionGraph(subGraph, inputEdge, subInputVertex, outputEdge, subOutputVertex);
    mainGraph.writeDOTFile(new File("build/merged.txt"));
  }
}
