package org.manifold.compiler.front;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.manifold.compiler.NodeTypeValue;
import org.manifold.compiler.NodeValue;
import org.manifold.compiler.SchematicValueVisitor;
import org.manifold.compiler.UndefinedBehaviourError;
import org.manifold.compiler.Value;
import org.manifold.compiler.middle.SchematicException;


public class PrimitiveFunctionValue extends FunctionValue {

  private final String primitiveName;
  public String getPrimitiveName() {
    return primitiveName;
  }
  
  private NodeTypeValue nodeType;
  
  public PrimitiveFunctionValue(String primitiveName, 
      FunctionTypeValue type, NodeTypeValue nodeType) { 
    super(type, new ArrayList<Expression>(0));
    this.primitiveName = primitiveName;
    this.nodeType = nodeType;
  }
  
  @Override
  public boolean isRuntimeKnowable() {
    return true;
  }
  
  @Override
  public boolean isElaborationtimeKnowable() {
    return false;
  }
  
  @Override
  public void accept(SchematicValueVisitor v) {
    if (v instanceof FrontendValueVisitor) {
      FrontendValueVisitor visitor = (FrontendValueVisitor) v;
      visitor.visit(this);
    } else {
      throw new UndefinedBehaviourError(
          "cannot accept non-frontend ValueVisitor into a frontend Value");
    }
  }
  
  public NodeValue elaborate() throws SchematicException {
    // TODO attributes
    Map<String, Value> attrs = new HashMap<>();
    // TODO port attributes
    Map<String, Map<String, Value>> portAttrMaps = new HashMap<>();
    for (String portName : nodeType.getPorts().keySet()) {
      portAttrMaps.put(portName, new HashMap<>());
    }
    
    NodeValue node = new NodeValue(nodeType, attrs, portAttrMaps);
    return node;
  }
  
}
