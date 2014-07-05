package org.manifold.intermediate;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

public class Port extends Value {

  private final Attributes attributes;
  private final Node parent;

  public Value getAttribute(String attrName)
      throws UndeclaredAttributeException {
    return attributes.get(attrName);
  }

  public Node getParent() {
    return parent;
  }

  public Port(PortType type, Node parent, Map<String, Value> attrMap)
      throws UndeclaredAttributeException, InvalidAttributeException {
    super(type);
    this.attributes = new Attributes(type.getAttributes(), attrMap);
    this.parent = checkNotNull(parent);
  }
}
