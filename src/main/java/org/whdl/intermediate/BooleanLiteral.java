package org.whdl.intermediate;

import org.whdl.intermediate.types.PrimitiveType;

public class BooleanLiteral extends DomainObject {

  private Boolean val;
  public BooleanLiteral(Boolean val){
    this.val = val;
  }
  
  @Override
  public Type accept(DomainObjectTypeVisitor v) {
    return v.visit(this);
  }

}
