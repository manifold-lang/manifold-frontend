package org.manifold.parser;

import java.util.LinkedList;
import java.util.List;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.manifold.compiler.IntegerValue;
import org.manifold.compiler.Value;
import org.manifold.compiler.front.Expression;
import org.manifold.compiler.front.LiteralExpression;
import org.manifold.compiler.front.VariableIdentifier;
import org.manifold.compiler.front.VariableReferenceExpression;

class ExpressionVisitor extends ManifoldBaseVisitor<Expression> {

  @Override
  public Expression visitNamespacedIdentifier(ManifoldParser.NamespacedIdentifierContext context) {

    List<TerminalNode> identifierNodes = context.IDENTIFIER();
    List<String> identifierStrings = new LinkedList<>();
    for (TerminalNode node: identifierNodes) {
      identifierStrings.add(node.getText());
    }
    
    VariableIdentifier variable = new VariableIdentifier(identifierStrings);

    return new VariableReferenceExpression(variable);
  }

  @Override
  public Expression visitTerminal(TerminalNode node) {
    if (node.getSymbol().getType() == ManifoldLexer.INTEGER_VALUE) {
      Value value = new IntegerValue(Integer.valueOf(node.getText()));
      return new LiteralExpression(value);
    } else {
      assert(false);
      return null;
    }
  }

}
