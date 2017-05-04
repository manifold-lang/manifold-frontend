package org.manifold.compiler.front;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.manifold.compiler.BooleanTypeValue;
import org.manifold.compiler.NilTypeValue;
import org.manifold.compiler.PortTypeValue;
import org.manifold.compiler.Value;

public class TestPrimitivePortVertex {

  @Test
  public void testConstructorSetsEdgeTargets() {
    ExpressionGraph g = new ExpressionGraph();
    ConstantValueVertex vSignalType = new ConstantValueVertex(g,
        BooleanTypeValue.getInstance());
    ExpressionEdge eSignalType = new ExpressionEdge(vSignalType, null);
    g.addVertex(vSignalType);
    g.addEdge(eSignalType);
    ConstantValueVertex vAttributes = new ConstantValueVertex(g,
        NilTypeValue.getInstance());
    ExpressionEdge eAttributes = new ExpressionEdge(vAttributes, null);
    g.addVertex(vAttributes);
    g.addEdge(eAttributes);
    PrimitivePortVertex vPort = new PrimitivePortVertex(g,
        eSignalType, eAttributes);
    g.addVertex(vPort);
    
    assertEquals(vPort, eSignalType.getTarget());
    assertEquals(vPort, eAttributes.getTarget());
  }
  
  @Test
  public void testElaborate_noAttributes() throws Exception {
    ExpressionGraph g = new ExpressionGraph();
    ConstantValueVertex vSignalType = new ConstantValueVertex(g,
        BooleanTypeValue.getInstance());
    ExpressionEdge eSignalType = new ExpressionEdge(vSignalType, null);
    g.addVertex(vSignalType);
    g.addEdge(eSignalType);
    ConstantValueVertex vAttributes = new ConstantValueVertex(g,
        NilTypeValue.getInstance());
    ExpressionEdge eAttributes = new ExpressionEdge(vAttributes, null);
    g.addVertex(vAttributes);
    g.addEdge(eAttributes);
    PrimitivePortVertex vPort = new PrimitivePortVertex(g,
        eSignalType, eAttributes);
    g.addVertex(vPort);
    
    vPort.elaborate();
    Value v = vPort.getValue();
    assertTrue(v instanceof PortTypeValue);
    PortTypeValue port = (PortTypeValue) v;
    // signal type = Bool
    assertTrue(port.getSignalType()
        .isSubtypeOf(BooleanTypeValue.getInstance()));
    // no attributes
    assertTrue(port.getAttributes().isEmpty());
  }
  
}
