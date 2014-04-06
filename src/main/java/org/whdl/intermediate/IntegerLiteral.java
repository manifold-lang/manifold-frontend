package org.whdl.intermediate;

import org.whdl.intermediate.types.PrimitiveType;

public class IntegerLiteral extends DomainObject {

  private Integer val;
  public IntegerLiteral(Integer val){
    this.val = val;
  }
  
  @Override
  public Type acceptVisitor(DomainObjectTypeVisitor v){
    return v.visit(this);
  }
  
}
