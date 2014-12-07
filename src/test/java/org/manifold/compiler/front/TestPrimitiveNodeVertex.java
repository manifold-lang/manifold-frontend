package org.manifold.compiler.front;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.LogManager;
import org.apache.log4j.PatternLayout;
import org.junit.BeforeClass;
import org.junit.Test;
import org.manifold.compiler.BooleanTypeValue;
import org.manifold.compiler.NilTypeValue;
import org.manifold.compiler.NodeTypeValue;
import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

public class TestPrimitiveNodeVertex {

  @BeforeClass
  public static void setupLogging() {
    PatternLayout layout = new PatternLayout(
        "%-5p [%t]: %m%n");
    LogManager.getRootLogger().removeAllAppenders();
    LogManager.getRootLogger().addAppender(
        new ConsoleAppender(layout, ConsoleAppender.SYSTEM_ERR));
  }
  
  private PrimitivePortVertex generatePort(ExpressionGraph g,
      TypeValue signalType) {
    ConstantValueVertex vSignalType = new ConstantValueVertex(g, signalType);
    ExpressionEdge eSignalType = new ExpressionEdge(vSignalType, null);
    g.addNonVariableVertex(vSignalType);
    g.addEdge(eSignalType);
    ConstantValueVertex vAttributes = new ConstantValueVertex(g,
        NilTypeValue.getInstance());
    ExpressionEdge eAttributes = new ExpressionEdge(vAttributes, null);
    g.addNonVariableVertex(vAttributes);
    g.addEdge(eAttributes);
    PrimitivePortVertex vPort = new PrimitivePortVertex(g,
        eSignalType, eAttributes);
    g.addNonVariableVertex(vPort);
    return vPort;
  }
  
  @Test
  public void testConstructorSetsEdgeTargets() 
      throws MultipleDefinitionException, VariableNotDefinedException {
    ExpressionGraph g = new ExpressionGraph();
    NamespaceIdentifier defaultNamespace = new NamespaceIdentifier("");
    
    PrimitivePortVertex vInPort = generatePort(g, 
        BooleanTypeValue.getInstance());
    VariableIdentifier idXIn = new VariableIdentifier(
        defaultNamespace, "xIn");
    g.createVariableVertex(idXIn);
    VariableReferenceVertex xIn = g.getVariableVertex(idXIn);
    // xIn = vInPort
    ExpressionEdge eIn = new ExpressionEdge(vInPort, xIn);
    g.addEdge(eIn);

    PrimitivePortVertex vOutPort = generatePort(g, 
        BooleanTypeValue.getInstance());
    VariableIdentifier idXOut = new VariableIdentifier(
        defaultNamespace, "xOut");
    g.createVariableVertex(idXOut);
    VariableReferenceVertex xOut = g.getVariableVertex(idXOut);
    // xOut = vOutPort
    ExpressionEdge eOut = new ExpressionEdge(vOutPort, xOut);
    g.addEdge(eOut);
    
    Map<String, ExpressionEdge> inputMap = new HashMap<>();
    ExpressionEdge eInputType = new ExpressionEdge(xIn, null);
    g.addEdge(eInputType);
    inputMap.put("x", eInputType);
    TupleTypeValueVertex vInputType = new TupleTypeValueVertex(g,
        inputMap, new HashMap<>());
    g.addNonVariableVertex(vInputType);
    
    Map<String, ExpressionEdge> outputMap = new HashMap<>();
    ExpressionEdge eOutputType = new ExpressionEdge(xOut, null);
    g.addEdge(eOutputType);
    outputMap.put("xbar", eOutputType);
    TupleTypeValueVertex vOutputType = new TupleTypeValueVertex(g,
        outputMap, new HashMap<>());
    g.addNonVariableVertex(vOutputType);
    
    ExpressionEdge eInputTuple = new ExpressionEdge(vInputType, null);
    g.addEdge(eInputTuple);
    ExpressionEdge eOutputTuple = new ExpressionEdge(vOutputType, null);
    g.addEdge(eOutputTuple);
    
    FunctionTypeValueVertex vNodePorts = new FunctionTypeValueVertex(g, 
        eInputTuple, eOutputTuple);
    g.addNonVariableVertex(vNodePorts);
    ExpressionEdge eNodePorts = new ExpressionEdge(vNodePorts, null);
    g.addEdge(eNodePorts);
    
    ConstantValueVertex vAttributes = new ConstantValueVertex(g,
        NilTypeValue.getInstance());
    ExpressionEdge eAttributes = new ExpressionEdge(vAttributes, null);
    g.addNonVariableVertex(vAttributes);
    g.addEdge(eAttributes);
    
    PrimitiveNodeVertex vNode = new PrimitiveNodeVertex(g,
        eNodePorts, eAttributes);
    g.addNonVariableVertex(vNode);
    
    assertEquals(vNode, eNodePorts.getTarget());
    assertEquals(vNode, eAttributes.getTarget());
  }
  
  @Test
  public void testElaborate_noAttributes() 
      throws Exception {
    ExpressionGraph g = new ExpressionGraph();
    NamespaceIdentifier defaultNamespace = new NamespaceIdentifier("");
    
    PrimitivePortVertex vInPort = generatePort(g, 
        BooleanTypeValue.getInstance());
    VariableIdentifier idXIn = new VariableIdentifier(
        defaultNamespace, "xIn");
    g.createVariableVertex(idXIn);
    VariableReferenceVertex xIn = g.getVariableVertex(idXIn);
    // xIn = vInPort
    ExpressionEdge eIn = new ExpressionEdge(vInPort, xIn);
    g.addEdge(eIn);

    PrimitivePortVertex vOutPort = generatePort(g, 
        BooleanTypeValue.getInstance());
    VariableIdentifier idXOut = new VariableIdentifier(
        defaultNamespace, "xOut");
    g.createVariableVertex(idXOut);
    VariableReferenceVertex xOut = g.getVariableVertex(idXOut);
    // xOut = vOutPort
    ExpressionEdge eOut = new ExpressionEdge(vOutPort, xOut);
    g.addEdge(eOut);
    
    Map<String, ExpressionEdge> inputMap = new HashMap<>();
    ExpressionEdge eInputType = new ExpressionEdge(xIn, null);
    g.addEdge(eInputType);
    inputMap.put("x", eInputType);
    TupleTypeValueVertex vInputType = new TupleTypeValueVertex(g,
        inputMap, new HashMap<>());
    g.addNonVariableVertex(vInputType);
    
    Map<String, ExpressionEdge> outputMap = new HashMap<>();
    ExpressionEdge eOutputType = new ExpressionEdge(xOut, null);
    g.addEdge(eOutputType);
    outputMap.put("xbar", eOutputType);
    TupleTypeValueVertex vOutputType = new TupleTypeValueVertex(g,
        outputMap, new HashMap<>());
    g.addNonVariableVertex(vOutputType);
    
    ExpressionEdge eInputTuple = new ExpressionEdge(vInputType, null);
    g.addEdge(eInputTuple);
    ExpressionEdge eOutputTuple = new ExpressionEdge(vOutputType, null);
    g.addEdge(eOutputTuple);
    
    FunctionTypeValueVertex vNodePorts = new FunctionTypeValueVertex(g, 
        eInputTuple, eOutputTuple);
    g.addNonVariableVertex(vNodePorts);
    ExpressionEdge eNodePorts = new ExpressionEdge(vNodePorts, null);
    g.addEdge(eNodePorts);
    
    ConstantValueVertex vAttributes = new ConstantValueVertex(g,
        NilTypeValue.getInstance());
    ExpressionEdge eAttributes = new ExpressionEdge(vAttributes, null);
    g.addNonVariableVertex(vAttributes);
    g.addEdge(eAttributes);
    
    PrimitiveNodeVertex vNode = new PrimitiveNodeVertex(g,
        eNodePorts, eAttributes);
    g.addNonVariableVertex(vNode);
    
    vNode.elaborate();
    Value v = vNode.getValue();
    assertTrue(v instanceof NodeTypeValue);
    NodeTypeValue node = (NodeTypeValue) v;
    // node has two ports
    assertTrue("node does not have two ports",
        node.getPorts().size() == 2);
    // node has a port called 'x'
    assertTrue("node does not have 'x' port",
        node.getPorts().containsKey("x"));
    // port 'x' on the node is an xIn
    assertEquals("'x' port is not of type xIn",
        xIn.getValue(), node.getPorts().get("x"));
    // node has a port called 'xbar'
    assertTrue("node does not have 'xbar' port",
        node.getPorts().containsKey("xbar"));
    // port 'xbar' on the node is an xOut
    assertEquals("'xbar' port is not of type xOut",
        xOut.getValue(), node.getPorts().get("xbar"));
    // node has no attributes
    assertTrue("node should not have any attributes",
        node.getAttributes().isEmpty());
  }
  
}
