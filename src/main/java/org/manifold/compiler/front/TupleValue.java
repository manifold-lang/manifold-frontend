package org.manifold.compiler.front;

import java.util.List;

import org.manifold.compiler.SchematicValueVisitor;
import org.manifold.compiler.UndefinedBehaviourError;
import org.manifold.compiler.Value;

public class TupleValue extends Value {

  private final List<Expression> entries;
  
  public int getSize() {
    return entries.size();
  }
  
  public Expression entry(int i) {
    return entries.get(i);
  }
  
  public Value getValue(int i, Scope scope) {
    return entry(i).getValue(scope);
  }
  
  public TupleValue(TupleTypeValue type, List<Expression> values) {
    super(type);
    // TODO check values against expected types
    this.entries = values;
  }
  
  @Override
  public boolean isElaborationtimeKnowable() {
    return true;
  }

  @Override
  public boolean isRuntimeKnowable() {
    return false;
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
