package org.manifold.compiler.front;

import java.util.HashMap;
import java.util.Map;

import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

import com.google.common.collect.ImmutableMap;

public class TupleTypeValueExpression extends Expression {

  private Map<String, Expression> typeValueExpressions;
  public Map<String, Expression> getTypeValueExpressions() {
    return ImmutableMap.copyOf(typeValueExpressions);
  }
  
  private Map<String, Expression> defaultValueExpressions;
  public Map<String, Expression> getDefaultValueExpressions() {
    return ImmutableMap.copyOf(defaultValueExpressions);
  }
  
  public TupleTypeValueExpression(
      Map<String, Expression> typeValueExpressions,
      Map<String, Expression> defaultValueExpressions) {
    this.typeValueExpressions = new HashMap<>(typeValueExpressions);
    this.defaultValueExpressions = new HashMap<>(defaultValueExpressions);
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
  public void accept(ExpressionVisitor visitor) {
    visitor.visit(this);
  }
  
}
