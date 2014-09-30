package org.manifold.compiler.front;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.manifold.compiler.BooleanTypeValue;
import org.manifold.compiler.middle.Schematic;

import com.google.common.collect.ImmutableList;

public class TestExpressionGraph {

  @Test
  public void testBuild_Simple() throws Exception {
    /*
     * Create expressions for this code: a = inputPin() outputPin(a) 
     * After building the expression graph we should see 
     * inputPin ---> a ---> outputPin
     */
    VariableIdentifier idInputPin = new VariableIdentifier(
        Arrays.asList(new String[]{"inputPin"}));
    VariableIdentifier idA = new VariableIdentifier(
        Arrays.asList(new String[]{"a"}));
    VariableIdentifier idOutputPin = new VariableIdentifier(
        Arrays.asList(new String[]{"outputPin"}));

    Expression assignInputPinToA = new VariableAssignmentExpression(
        new VariableReferenceExpression(idA), new FunctionInvocationExpression(
            new VariableReferenceExpression(idInputPin), new LiteralExpression(
                new TupleValue(new TupleTypeValue(ImmutableList.of()),
                    ImmutableList.of()))));

    Expression aToOutputPin = new FunctionInvocationExpression(
        new VariableReferenceExpression(idOutputPin), new LiteralExpression(
            new TupleValue(new TupleTypeValue(ImmutableList.of(BooleanTypeValue
                .getInstance())),
                ImmutableList.of(new VariableReferenceExpression(idA)))));
    List<Expression> exprs = Arrays.asList(new Expression[] {
      assignInputPinToA, aToOutputPin });

    Scope toplevel = new Scope();
    Schematic schematic = new Schematic("test");

    Main.createDigitalPrimitives(toplevel, schematic);
    ExpressionGraph graph = new ExpressionGraph(toplevel);
    graph.buildFrom(exprs);
    graph.removeUnconnectedEdges();
    
    List<PrimitiveFunctionVertex> primitives = 
        graph.getPrimitiveFunctionVertices();
    Map<VariableIdentifier, VariableReferenceVertex> variables = 
        graph.getVariableVertices();

    // look for primitives
    PrimitiveFunctionVertex vInputPin = null;
    PrimitiveFunctionVertex vOutputPin = null;
    for (PrimitiveFunctionVertex v : primitives) {
      String primitiveName = v.getFunction().getPrimitiveName();
      if (primitiveName.equals("inputPin")) {
        vInputPin = v;
      } else if (primitiveName.equals("outputPin")) {
        vOutputPin = v;
      }
    }
    if (vInputPin == null) {
      fail("input pin primitive not found in generated expression graph");
    }
    if (vOutputPin == null) {
      fail("output pin primitive not found in generated expression graph");
    }
    
    // look for variables
    if (!variables.containsKey(idA)) {
      fail("variable reference vertex for 'a' not found" 
          + " in generated expression graph");
    }
    VariableReferenceVertex vA = variables.get(idA);
    
    // look for an edge (vInputPin) ---> (vA)
    for (ExpressionEdge e : graph.getEdgesFromSource(vInputPin)) {
      assertEquals("unexpected edge from " + vInputPin.toString(),
          vA, e.getTarget());
    }
    
    // look for an edge (vA) --(tuple)-> (vOutputPin)
    for (ExpressionEdge e : graph.getEdgesFromSource(vA)) {
      // assume the target of this edge is the tuple...
      for (ExpressionEdge eTuple : graph.getEdgesFromSource(e.getTarget())) {
        assertEquals("unexpected edge from " + vA.toString() + " through " 
            + eTuple.getSource().toString(),
            vOutputPin, eTuple.getTarget());
      }
    }
  }

}
