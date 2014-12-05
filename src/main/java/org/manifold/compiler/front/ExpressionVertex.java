package org.manifold.compiler.front;

import java.io.BufferedWriter;
import java.io.IOException;

import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

public abstract class ExpressionVertex {

  public abstract TypeValue getType();
  public abstract Value getValue();
  public abstract void verify() throws Exception;
  public abstract boolean isElaborationtimeKnowable();
  public abstract boolean isRuntimeKnowable();
  public abstract void writeToDOTFile(BufferedWriter writer) throws IOException;

}
