package org.manifold.intermediate;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

public class Connection extends Value {

  private final Attributes attributes;

  public Value getAttribute(String attrName) throws
      UndeclaredAttributeException {
    return attributes.get(attrName);
  }
  
  private Port portFrom = null, portTo = null;
  
  public Port getFrom() {
    return portFrom;
  }
  
  public Port getTo() {
    return portTo;
  }

  public Connection(ConnectionType type, Port from, Port to,
      Map<String, Value> attrs)
      throws UndeclaredAttributeException, InvalidAttributeException {
    super(type);
    this.attributes = new Attributes(type.getAttributes(), attrs);
    this.portFrom = checkNotNull(from);
    this.portTo = checkNotNull(to);
    
    if (from == to) {
      throw new UndefinedBehaviourError(
        "Cannot create connection from a port to itself"
      );
    }
  }

}
