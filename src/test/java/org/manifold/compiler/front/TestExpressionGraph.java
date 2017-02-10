package org.manifold.compiler.front;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.LogManager;
import org.apache.log4j.PatternLayout;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.manifold.compiler.BooleanValue;
import org.manifold.compiler.StringTypeValue;
import org.manifold.compiler.StringValue;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TestExpressionGraph {
  ExpressionGraph expressionGraph;

  @BeforeClass
  public static void setupLogging() {
    PatternLayout layout = new PatternLayout(
        "%-5p [%t]: %m%n");
    LogManager.getRootLogger().removeAllAppenders();
    LogManager.getRootLogger().addAppender(
        new ConsoleAppender(layout, ConsoleAppender.SYSTEM_ERR));
  }
  
  @AfterClass
  public static void afterClass() {
    LogManager.getRootLogger().removeAllAppenders();
  }
  
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
    subGraph.addVertex(subInput);
    ExpressionVertex subInputEdgeVertex = subGraph.getVariableVertex(subInput);
    ExpressionEdge subGraphInputEdge = new ExpressionEdge(null, subInputEdgeVertex);
    subGraph.addEdge(subGraphInputEdge);

    VariableIdentifier dummyReturn = new VariableIdentifier(ImmutableList.of("returnValue"));
    subGraph.addVertex(dummyReturn);
    ExpressionVertex dummyReturnVertex = subGraph.getVariableVertex(dummyReturn);
    ExpressionEdge returnEdge = new ExpressionEdge(dummyReturnVertex, null);
    subGraph.addEdge(returnEdge);

    TupleValueVertex subInputVertex = new TupleValueVertex(subGraph,
            new MappedArray<>(ImmutableMap.of("a", subGraphInputEdge)));
    TupleValueVertex subOutputVertex = new TupleValueVertex(subGraph,
            new MappedArray<>(ImmutableMap.of("a", returnEdge)));

    subGraph.addVertex(subInputVertex);
    subGraph.addVertex(subOutputVertex);

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

    Map<VariableReferenceVertex, VariableReferenceVertex> dontRename = new HashMap<>();
    mainGraph.addFunctionExpressionGraph(subGraph, inputEdge, subInputVertex, outputEdge, subOutputVertex, dontRename);
    mainGraph.writeDOTFile(new File("build/merged.txt"));
  }
}
