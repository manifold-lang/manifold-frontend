package org.manifold.compiler.front;

import java.io.BufferedWriter;
import java.io.IOException;

import org.manifold.compiler.TypeValue;

public abstract class ExpressionVertex {

  private TypeValue type = UnknownTypeValue.getInstance();
  public TypeValue getType() {
    return this.type;
  }
  public void setType(TypeValue type) {
    this.type = type;
  }

  public abstract void writeToDOTFile(BufferedWriter writer) throws IOException;

}
