package org.manifold.compiler.front;

import java.util.HashMap;
import java.util.Map;

import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

import com.google.common.collect.ImmutableMap;

public class TupleValueExpression extends Expression {

  private Map<String, Expression> valueExpressions;
  public Map<String, Expression> getValueExpressions() {
    return ImmutableMap.copyOf(valueExpressions);
  }
  
  public TupleValueExpression(Map<String, Expression> valueExpressions) {
    this.valueExpressions = new HashMap<>(valueExpressions);
  }
  
  @Override
  public TypeValue getType(Scope scope) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Value getValue(Scope scope) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void verify(Scope scope) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean isAssignable() {
    return false;
  }

  @Override
  public boolean isElaborationtimeKnowable(Scope scope) {
    return true;
  }

  @Override
  public boolean isRuntimeKnowable(Scope scope) {
    return true;
  }

  @Override
  public void accept(ExpressionVisitor visitor) throws Exception {
    visitor.visit(this);
  }
  
}
