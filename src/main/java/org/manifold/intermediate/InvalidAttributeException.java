package org.manifold.intermediate;

public class InvalidAttributeException extends SchematicException {
  private static final long serialVersionUID = 3865358184369013184L;

  public String name;

  public InvalidAttributeException(String name) {
    this.name = name;
  }

  @Override
  public String getMessage() {
    return "invalid attribute '" + this.name + "'";
  }
}
