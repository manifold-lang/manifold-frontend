package org.manifold.intermediate;

import java.util.HashMap;
import java.util.Map;

public class Node extends Value {

  private final Attributes attributes;
  private final Map<String, Port> ports;

  public Value getAttribute(String attrName)
      throws UndeclaredAttributeException {
    return attributes.get(attrName);
  }

  public Port getPort(String portName) throws UndeclaredIdentifierException{
    if (ports.containsKey(portName)){
      return ports.get(portName);
    } else {
      throw new UndeclaredIdentifierException(portName);
    }
  }

  public Node(NodeType type, Map<String, Value> attrs,
      Map<String, Map<String, Value>> portAttrMaps)
      throws UndeclaredIdentifierException, UndeclaredAttributeException {
    super(type);
    this.attributes = new Attributes(type.getAttributes(), attrs);
    this.ports = new HashMap<>();

    final Map<String, PortType> portTypes = type.getPorts();
    if (portTypes != null) {
      for (String portName : portAttrMaps.keySet()) {
        if (!portTypes.containsKey(portName)) {
          throw new UndeclaredIdentifierException(portName);
        }
      }
      for (Map.Entry<String, PortType> portEntry : type.getPorts().entrySet()) {
        String portName = portEntry.getKey();
        PortType portType = portEntry.getValue();
        Map<String, Value> portAttrs = portAttrMaps.get(portName);
        if (portAttrs == null) {
          // TODO(TysonAndre): throw something more appropriate
          // than UndeclaredIdentifierException?
          throw new UndeclaredIdentifierException(portName);
        }
        this.ports.put(portName, new Port(portType, this, portAttrs));
      }
    }
  }
}
