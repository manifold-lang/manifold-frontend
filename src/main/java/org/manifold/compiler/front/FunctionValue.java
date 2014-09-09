package org.manifold.compiler.front;

import java.util.List;

import org.manifold.compiler.SchematicValueVisitor;
import org.manifold.compiler.UndefinedBehaviourError;
import org.manifold.compiler.Value;

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
  public void accept(SchematicValueVisitor v) {
    if (v instanceof FrontendValueVisitor) {
      FrontendValueVisitor visitor = (FrontendValueVisitor) v;
      visitor.visit(this);
    } else {
      throw new UndefinedBehaviourError(
          "cannot accept non-frontend ValueVisitor into a frontend Value");
    }
  }
  
}
