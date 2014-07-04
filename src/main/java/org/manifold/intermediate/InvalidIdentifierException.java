package org.manifold.intermediate;

public class InvalidIdentifierException extends SchematicException {
  private static final long serialVersionUID = -1571780250014515287L;

  public String identifier;

  public InvalidIdentifierException(String identifier) {
    this.identifier = identifier;
  }

  @Override
  public String getMessage() {
    return "invalid identifier '" + this.identifier + "'";
  }
}
