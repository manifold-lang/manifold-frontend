package org.manifold.compiler.front;

import org.manifold.compiler.TypeValue;

public interface NamedEntryTypeValue {
  TypeValue getEntry(String key) throws Exception;
}
