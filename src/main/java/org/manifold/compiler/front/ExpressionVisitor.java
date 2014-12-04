package org.manifold.compiler.front;

public interface ExpressionVisitor {

  void visit(FunctionInvocationExpression functionInvocationExpression);
  void visit(LiteralExpression literalExpression);
  void visit(VariableAssignmentExpression variableAssignmentExpression);
  void visit(VariableReferenceExpression variableReferenceExpression);
  void visit(PrimitivePortDefinitionExpression
      primitivePortDefinitionExpression);
  void visit(PrimitiveNodeDefinitionExpression
      primitiveNodeDefinitionExpression);
  void visit(TupleTypeValueExpression tupleTypeValueExpression);
  void visit(FunctionTypeValueExpression functionTypeValueExpression);

}
