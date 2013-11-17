package org.whdl.frontend.syntaxtree;

public abstract class Expression {
  public abstract Value evaluate();
  public abstract TypeValue getType();
}
