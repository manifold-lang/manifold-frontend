package org.manifold.compiler.front;

import org.manifold.compiler.UndefinedBehaviourError;

public class ExpressionEdge {

  private ExpressionVertex source;
  public ExpressionVertex getSource() {
    return source;
  }
  public void setSource(ExpressionVertex newSource) {
    if (! (source == null)) {
      throw new UndefinedBehaviourError(
          "attempt to change source of attached ExpressionEdge");
    }
    this.source = newSource;
  }
  
  private ExpressionVertex target;
  public ExpressionVertex getTarget() {
    return target;
  }
  public void setTarget(ExpressionVertex newTarget) {
    if (! (target == null)) {
      throw new UndefinedBehaviourError(
          "attempt to change target of attached ExpressionEdge");
    }
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
    return sourceStr + " => " + targetStr;
  }
  
}
