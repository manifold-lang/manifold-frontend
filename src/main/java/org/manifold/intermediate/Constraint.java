package org.manifold.intermediate;

import java.util.Map;

public class Constraint extends Value {

  private final Attributes attributes;

  public Value getAttribute(String attrName) throws
      UndeclaredAttributeException {
    return attributes.get(attrName);
  }

  public Constraint(ConstraintType type, Map<String, Value> attrs)
      throws UndeclaredAttributeException, InvalidAttributeException {
    super(type);
    this.attributes = new Attributes(type.getAttributes(), attrs);
  }
}
