package org.manifold.compiler.front;

public class GraphConstructionException extends Exception {
  private static final long serialVersionUID = 6204398860700824866L;
  
  private String message;
  public GraphConstructionException(String message) {
    this.message = message;
  }
  
  @Override
  public String getMessage() {
    return message;
  }
  
}
