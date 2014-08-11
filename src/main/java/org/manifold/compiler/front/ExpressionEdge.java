package org.manifold.compiler.front;

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
  
  public ExpressionEdge(ExpressionVertex from, ExpressionVertex to) {
    if (from == null && to == null) {
      throw new UndefinedBehaviourError(
          "attempt to create a totally disconnected ExpressionEdge");
    }
    source = from;
    target = to;
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
