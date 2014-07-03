package org.manifold.intermediate;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class Attributes {
  private final Map<String, Value> data;

  public Attributes(Map<String, Type> types, Map<String, Value> data)
      throws UndeclaredAttributeException {
    for (Map.Entry<String, Type> entry: types.entrySet()) {
      String attrName = entry.getKey();
      if (!data.containsKey(attrName)) {
        throw new UndeclaredAttributeException(attrName);
      }
      // TODO: Add attribute type checking in another diff.
    }
    for (String attrName: data.keySet()) {
      if (!types.containsKey(attrName)) {
        // TODO: Should this be a different type, or should there be a common base class?
        throw new UndeclaredAttributeException(attrName);
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