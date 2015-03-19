package org.manifold.compiler.front;

import org.manifold.compiler.SchematicValueVisitor;
import org.manifold.compiler.UndefinedBehaviourError;
import org.manifold.compiler.Value;

public class FunctionValue extends Value {

  private ExpressionGraph body;
  private TupleValueVertex vInput;
  private TupleValueVertex vOutput;

  public FunctionValue(FunctionTypeValue type, ExpressionGraph body,
      TupleValueVertex vInput, TupleValueVertex vOutput) {
    super(type);
    this.body = body;
    this.vInput = vInput;
    this.vOutput = vOutput;
  }

  @Override
  public void verify() throws Exception {
    // TODO introspect body to make sure it treats input and output values
    // correctly?
  }

  public ExpressionGraph getBody() {
    return body;
  }

  public TupleValueVertex getInputVertex() {
    return vInput;
  }

  public TupleValueVertex getOutputVertex() {
    return vOutput;
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
