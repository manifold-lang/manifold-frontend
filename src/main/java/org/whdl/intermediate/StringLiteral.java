package org.whdl.intermediate;

import org.whdl.intermediate.types.PrimitiveType;

public class StringLiteral extends DomainObject {

  private String val;
  public StringLiteral(String val){
    this.val = val;
  }
  
  @Override
  public Type accept(DomainObjectTypeVisitor v) {
    return v.visit(this);
  }
  
}
