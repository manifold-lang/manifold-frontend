package org.manifold.compiler.front;

import org.manifold.compiler.TypeValue;
import org.manifold.compiler.UndefinedBehaviourError;

public class ExpressionEdge {

  private String name = "";
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  
  private ExpressionVertex source;
  public ExpressionVertex getSource() {
    return source;
  }
  public void setSource(ExpressionVertex newSource) {
    this.source = newSource;
  }
  
  private ExpressionVertex target;
  public ExpressionVertex getTarget() {
    return target;
  }
  public void setTarget(ExpressionVertex newTarget) {
    this.target = newTarget;
  }
  
  private TypeValue type;
  public TypeValue getType() {
    return type;
  }
  public void setType(TypeValue t) {
    this.type = t;
  }
  
  public ExpressionEdge(ExpressionVertex from, ExpressionVertex to,
      TypeValue type) {
    if (from == null && to == null) {
      throw new UndefinedBehaviourError(
          "attempt to create a totally disconnected ExpressionEdge");
    }
    source = from;
    target = to;
    this.type = type;
  }

  @Override
  public String toString() {
    String sourceStr;
    if (source == null) {
      sourceStr = "(null)";
    } else {
      sourceStr = source.toString();
    }
    String targetStr;
    if (target == null) {
      targetStr = "(null)";
    } else {
      targetStr = target.toString();
    }
    return sourceStr + " ==" + getName() + "=> " + targetStr;
  }
  
}
