package org.manifold.compiler.front;

import java.util.List;

import org.manifold.compiler.Value;
import org.manifold.compiler.ValueVisitor;

public class FunctionValue extends Value {
  
  private final List<Expression> body;
  
  public FunctionValue(FunctionTypeValue type, List<Expression> body) {
    super(type);
    this.body = body;
  }
  
  @Override
  public void verify() throws Exception {
    // TODO introspect body to make sure it treats input and output values
    // correctly?
  }

  public List<Expression> getBody() {
    return body;
  }
  
  @Override
  public boolean isRuntimeKnowable() {
    return false;
  }
  
  @Override
  public boolean isElaborationtimeKnowable() {
    return true;
  }
  
  @Override
  public void accept(ValueVisitor visitor) {
    visitor.visit(this);
  }
  
}
