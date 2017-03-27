package org.manifold.compiler.front;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ExpressionEdge {

  private static Logger log = LogManager.getLogger(ExpressionEdge.class);
  
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
