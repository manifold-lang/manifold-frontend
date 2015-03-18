package org.manifold.compiler.front;

import org.manifold.compiler.SchematicValueVisitor;
import org.manifold.compiler.UndefinedBehaviourError;
import org.manifold.compiler.Value;

public class FunctionValue extends Value {
  
  private ExpressionGraph body;
  
  public FunctionValue(FunctionTypeValue type, ExpressionGraph body) {
    super(type);
    this.body = body;
  }
  
  @Override
  public void verify() throws Exception {
    // TODO introspect body to make sure it treats input and output values
    // correctly?
  }

  public ExpressionGraph getBody() {
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
    throw new UndefinedBehaviourError(
        "cannot accept non-frontend ValueVisitor into a frontend Value");
  }
  
}
