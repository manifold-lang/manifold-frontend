package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;

public class AttributeSet {
  private Map<String, Value> data;

  public AttributeSet() {
    data = new HashMap<String, Value>();
  }

  public Value get(String attrName) throws UndeclaredIdentifierException {
    if (data.containsKey(attrName)) {
      return data.get(attrName);
    } else {
      throw new UndeclaredIdentifierException("no attribute named '" + attrName
          + "'");
    }
  }

  public void put(String attrName, Value v) {
    data.put(attrName, v);
  }
}
