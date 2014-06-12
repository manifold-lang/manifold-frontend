package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;

public class Attributes {
  private Map<String, Value> data;

  public Attributes() {
    data = new HashMap<String, Value>();
  }

  public Value get(String attrName) throws UndeclaredAttributeException {
    if (data.containsKey(attrName)) {
      return data.get(attrName);
    } else {
      throw new UndeclaredAttributeException(attrName);
    }
  }

  public void put(String attrName, Value v) {
    data.put(attrName, v);
  }
}
