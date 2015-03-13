package org.manifold.compiler.front;

import org.manifold.compiler.SchematicValueVisitor;
import org.manifold.compiler.TypeValue;
import org.manifold.compiler.UndefinedBehaviourError;

public class UnknownTypeValue extends TypeValue {

  private static final UnknownTypeValue instance = new UnknownTypeValue();

  public static UnknownTypeValue getInstance() {
    return instance;
  }

  private UnknownTypeValue() { }

  @Override
  public void accept(SchematicValueVisitor v) {
    throw new UndefinedBehaviourError(
        "cannot accept non-frontend ValueVisitor into a frontend Value");
  }

}
