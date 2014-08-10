package org.manifold.compiler;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.manifold.compiler.front.Expression;
import org.manifold.compiler.front.FunctionInvocationExpression;
import org.manifold.compiler.front.LiteralExpression;
import org.manifold.compiler.front.Scope;
import org.manifold.compiler.front.TupleTypeValue;
import org.manifold.compiler.front.TupleValue;
import org.manifold.compiler.front.VariableAssignmentExpression;
import org.manifold.compiler.front.VariableIdentifier;
import org.manifold.compiler.front.VariableReferenceExpression;
import org.manifold.parser.ManifoldBaseVisitor;
import org.manifold.parser.ManifoldLexer;
import org.manifold.parser.ManifoldParser;
import org.manifold.parser.ManifoldParser.ExpressionContext;
import org.manifold.parser.ManifoldParser.NamespacedIdentifierContext;

public class Main {

  public static void main(String[] args) throws Exception {

    ManifoldLexer lexer = new ManifoldLexer(new ANTLRInputStream(
        new FileInputStream(args[0])));

     // Get a list of matched tokens
    CommonTokenStream tokens = new CommonTokenStream(lexer);

    // Pass the tokens to the parser
    ManifoldParser parser = new ManifoldParser(tokens);

    // Specify our entry point
    ManifoldParser.SchematicContext context = parser.schematic();
    
    ExpressionVisitor visitor = new ExpressionVisitor();

    List<Expression> expressions = new LinkedList<>();
    List<ExpressionContext> expressionContexts = context.expression();
    
    for (ExpressionContext expressionContext : expressionContexts) {
      expressions.add(visitor.visit(expressionContext));
    }
    
    // Build top-level scope
    Scope toplevel = new Scope();
    for (Expression expr : expressions) {
      if (expr instanceof VariableAssignmentExpression) {
        VariableAssignmentExpression assign = 
            (VariableAssignmentExpression) expr;
        Expression lvalue = assign.getLvalueExpression();
        Expression rvalue = assign.getRvalueExpression();
        
        // we expect the lvalue to be a variable reference
        if (lvalue instanceof VariableReferenceExpression) {
          VariableReferenceExpression vRef = 
              (VariableReferenceExpression) lvalue;
          VariableIdentifier identifier = vRef.getIdentifier();
          Expression idType = 
              new LiteralExpression(rvalue.getType(null)); // I hope this works
          toplevel.defineVariable(identifier, idType);
          toplevel.assignVariable(identifier, rvalue);
        } else {
          assert(false);
        }
      }
    }
    
    System.out.println("expressions:");
    System.out.print(expressions);
    System.out.println();
    System.out.println("top-level identifiers:");
    for(VariableIdentifier id : toplevel.getSymbolIdentifiers()) {
      System.out.println(id);
    }
    
  }

}

class ExpressionVisitor extends ManifoldBaseVisitor<Expression> {
  
  @Override
  public Expression visitAssignment(ManifoldParser.AssignmentContext context) {
    return new VariableAssignmentExpression(
        visit(context.expression(0)),
        visit(context.expression(1))
    );
  }
  
  @Override
  public Expression visitFunctionInvocation(
      ManifoldParser.FunctionInvocationContext context) {
    return new FunctionInvocationExpression (
        visit(context.expression(0)),
        visit(context.expression(1))
    );
  }
  
  @Override
  public Expression visitTupleValue(ManifoldParser.TupleValueContext context) {
    // get the expressions resulting from visiting all value entries
    List<Expression> values = new ArrayList<Expression>();
    for (ManifoldParser.TupleValueEntryContext subctx 
        : context.tupleValueEntry()) {
      // TODO named value entries
      values.add(visit(subctx.expression()));
    }
    // construct a type
    Scope emptyScope = new Scope();
    List<TypeValue> types = new ArrayList<TypeValue>();
    for (Expression e : values) {
      types.add(e.getType(emptyScope));
    }
    TupleTypeValue anonymousTupleType = new TupleTypeValue(types);
    // now we build a TupleValue from these subexpressions
    return new LiteralExpression(new TupleValue(anonymousTupleType, values));
  }
  
  @Override
  public Expression visitNamespacedIdentifier(
      NamespacedIdentifierContext context) {

    List<TerminalNode> identifierNodes = context.IDENTIFIER();
    List<String> identifierStrings = new LinkedList<>();
    for (TerminalNode node : identifierNodes) {
      identifierStrings.add(node.getText());
    }
    
    VariableIdentifier variable = new VariableIdentifier(identifierStrings);

    return new VariableReferenceExpression(variable);
  }

  @Override
  public Expression visitTerminal(TerminalNode node) {
    if (node.getSymbol().getType() == ManifoldLexer.INTEGER_VALUE) {
      return new LiteralExpression(
          new IntegerValue(Integer.valueOf(node.getText()))
      );
      
    } else if (node.getSymbol().getType() == ManifoldLexer.BOOLEAN_VALUE) {
      return new LiteralExpression(
        BooleanValue.getInstance(Boolean.parseBoolean(node.getText()))
      );
        
    } else {
      assert(false);
      return null;
    }
  }

}
