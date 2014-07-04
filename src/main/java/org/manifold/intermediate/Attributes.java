package org.manifold.intermediate;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class Attributes {
  private final Map<String, Value> data;

  public Attributes(Map<String, Type> types, Map<String, Value> data)
      throws UndeclaredAttributeException, InvalidAttributeException {
    for (Map.Entry<String, Type> entry : types.entrySet()) {
      String attrName = entry.getKey();
      if (!data.containsKey(attrName)) {
        throw new UndeclaredAttributeException(attrName);
      }
      // TODO: Add attribute type checking in another diff.
    }
    for (String attrName : data.keySet()) {
      if (!types.containsKey(attrName)) {
        throw new InvalidAttributeException(attrName);
      }
    }
    this.data = ImmutableMap.copyOf(data);
  }

  public Value get(String attrName) throws UndeclaredAttributeException {
    if (data.containsKey(attrName)) {
      return data.get(attrName);
    } else {
      throw new UndeclaredAttributeException(attrName);
    }
  }
}
