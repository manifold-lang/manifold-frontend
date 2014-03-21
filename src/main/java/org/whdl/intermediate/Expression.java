package org.whdl.intermediate;

public interface Expression {
  Type accept(ExprTypeVisitor v);
  Object accept(ExprEvalVisitor v);
}
