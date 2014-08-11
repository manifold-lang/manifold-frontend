package org.manifold.compiler.front;

import java.util.Map.Entry;

import org.manifold.compiler.NodeValue;
import org.manifold.compiler.PortTypeValue;
import org.manifold.compiler.PortValue;
import org.manifold.compiler.middle.SchematicException;

public class PrimitiveFunctionVertex extends ExpressionVertex {

  private PrimitiveFunctionValue function;
  
  public PrimitiveFunctionVertex(PrimitiveFunctionValue function) {
    this.function = function;
  }
  
  @Override
  public String toString() {
    return "primitive function " + function.getPrimitiveName();
  }

  private NodeValue elaboratedNode = null;
  
  public void elaborate() throws SchematicException {
    elaboratedNode = function.elaborate();
  }
  
  public NodeValue getNodeValue() {
    return elaboratedNode;
  }
  
  private int uniqueSuffix = 0;
  public void setUniqueSuffix(int uuid) {
    uniqueSuffix = uuid;
  }
  
  public String getInstanceName() {
    return function.getPrimitiveName() + Integer.toString(uniqueSuffix);
  }
  
  public int getNumberOfPortsOfType(PortTypeValue portType) {
    int i = 0;
    for (PortValue port : elaboratedNode.getPorts().values()) {
      if (port.getType().isSubtypeOf(portType)) {
        ++i;
      }
    }
    return i;
  }
  
  public String getNthPortOfType(PortTypeValue portType, int n) {
    int i = 0;
    for (Entry<String, PortValue> entry : 
      elaboratedNode.getPorts().entrySet()) {
      if (entry.getValue().getType().isSubtypeOf(portType)) {
        if (i == n){
          return entry.getKey(); 
        } else {
          ++i;
        }
      }
    }
    throw new ArrayIndexOutOfBoundsException(n);
  }
  
}
