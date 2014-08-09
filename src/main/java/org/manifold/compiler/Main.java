package org.manifold.compiler;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import java.util.LinkedList;
import java.util.List;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.manifold.compiler.front.Expression;
import org.manifold.compiler.front.LiteralExpression;
import org.manifold.compiler.front.VariableIdentifier;
import org.manifold.compiler.front.VariableReferenceExpression;
import org.manifold.parser.ManifoldBaseVisitor;
import org.manifold.parser.ManifoldLexer;
import org.manifold.parser.ManifoldParser;

public class Main {

  public static void main(String[] args) throws Exception {

    ManifoldLexer lexer = new ManifoldLexer(new ANTLRInputStream(System.in));

     // Get a list of matched tokens
    CommonTokenStream tokens = new CommonTokenStream(lexer);

    // Pass the tokens to the parser
    ManifoldParser parser = new ManifoldParser(tokens);

    // Specify our entry point
    ManifoldParser.SchematicContext context = parser.schematic();
    
    ExpressionVisitor visitor = new ExpressionVisitor();

    List<Expression> expressions = new LinkedList<>();
    List<ManifoldParser.ExpressionContext> expressionContexts = context.expression();
    
    for (ManifoldParser.ExpressionContext expressionContext: expressionContexts) {
      expressions.add(visitor.visit(expressionContext));
    }    
  }
}

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