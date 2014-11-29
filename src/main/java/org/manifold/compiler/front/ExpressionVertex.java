package org.manifold.compiler.front;

import java.io.BufferedWriter;
import java.io.IOException;

public abstract class ExpressionVertex {
  
  public abstract void writeToDOTFile(BufferedWriter writer) throws IOException;
  
}
