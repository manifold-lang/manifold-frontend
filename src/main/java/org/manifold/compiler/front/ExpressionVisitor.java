package org.manifold.compiler.front;

public interface ExpressionVisitor {

  void visit(FunctionInvocationExpression functionInvocationExpression);
  void visit(LiteralExpression literalExpression);
  void visit(VariableAssignmentExpression variableAssignmentExpression);
  void visit(VariableReferenceExpression variableReferenceExpression);

}
