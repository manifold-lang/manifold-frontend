package org.manifold.compiler.front;

import org.manifold.compiler.SchematicValueVisitor;

public interface FrontendValueVisitor extends SchematicValueVisitor {
  public void visit(TupleValue tuple);

  public void visit(TupleTypeValue tupleTypeValue);

  public void visit(FunctionValue functionValue);

  public void visit(FunctionTypeValue functionTypeValue);

  public void visit(EnumValue enumValue);

  public void visit(EnumTypeValue enumTypeValue);

  public void visit(PrimitivePortDefinitionExpression primitivePortTypeValue);

  public void visit(UnknownTypeValue unknownTypeValue);
}
