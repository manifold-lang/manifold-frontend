package org.manifold.intermediate;

import static com.google.common.base.Preconditions.checkNotNull;

public class Connection extends Value {

  private Attributes attributes;

  public Value getAttribute(String attrName) throws UndeclaredAttributeException {
    return attributes.get(attrName);
  }
  public void setAttribute(String attrName, Value attrValue){
    attributes.put(attrName, attrValue);
  }
  
  private Port portFrom = null, portTo = null;
  
  public Port getFrom() {
    return portFrom;
  }
  
  public Port getTo() {
    return portTo;
  }

  public Connection(ConnectionType type, Port from, Port to){
    super(type);
    this.attributes = new Attributes();
    this.portFrom = checkNotNull(from);
    this.portTo = checkNotNull(to);
    
    if (from == to) {
      throw new UndefinedBehaviourError("Cannot create connection from a port to itself");
    }
  }

}
