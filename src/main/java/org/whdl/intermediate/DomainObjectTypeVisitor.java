package org.whdl.intermediate;

import org.whdl.intermediate.types.*;

public class DomainObjectTypeVisitor {

  public Type visit(BooleanLiteral e){
    return PrimitiveType.BOOLEAN;
  }
  
  public Type visit(IntegerLiteral e){
    return PrimitiveType.INTEGER;
  }
  
  public Type visit(StringLiteral e){
    return PrimitiveType.STRING;
  }

}
