package org.manifold.compiler.front;

import org.manifold.compiler.Value;

public interface NamedEntryValue {
  Value getEntry(String key) throws Exception;
}
