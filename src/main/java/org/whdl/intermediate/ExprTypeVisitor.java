package org.whdl.intermediate;

import org.whdl.intermediate.expressions.*;
import org.whdl.intermediate.types.*;

public class ExprTypeVisitor {

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
