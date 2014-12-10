package org.manifold.compiler.front;

public interface ExpressionVisitor {

  void visit(FunctionInvocationExpression functionInvocationExpression)
    throws Exception;
  void visit(LiteralExpression literalExpression) throws Exception;
  void visit(VariableAssignmentExpression variableAssignmentExpression) 
      throws Exception;
  void visit(VariableReferenceExpression variableReferenceExpression) 
      throws Exception;
  void visit(PrimitivePortDefinitionExpression
      primitivePortDefinitionExpression) throws Exception;
  void visit(PrimitiveNodeDefinitionExpression
      primitiveNodeDefinitionExpression) throws Exception;
  void visit(TupleTypeValueExpression tupleTypeValueExpression) 
      throws Exception;
  void visit(FunctionTypeValueExpression functionTypeValueExpression) 
      throws Exception;

}
