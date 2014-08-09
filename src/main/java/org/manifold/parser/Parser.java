package org.manifold.parser;

import java.util.LinkedList;
import java.util.List;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.manifold.parser.ExpressionVisitor;
import org.manifold.compiler.front.Expression;

public class Parser {
 
  public List<Expression> parse(ANTLRInputStream input) {    
    
    ManifoldLexer lexer = new ManifoldLexer(input);

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
    
    return expressions;
  }
  
}
