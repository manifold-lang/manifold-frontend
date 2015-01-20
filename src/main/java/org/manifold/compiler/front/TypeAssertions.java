package org.manifold.compiler.front;

import org.manifold.compiler.TypeTypeValue;
import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

public class TypeAssertions {

  public static TypeValue assertIsType(Value v) throws TypeMismatchException {
    if (v != null && v instanceof TypeValue) {
      return (TypeValue) v;
    } else {
      throw new TypeMismatchException(TypeTypeValue.getInstance(), v);
    }
  }
  
}
