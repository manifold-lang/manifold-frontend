package org.manifold.compiler;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.manifold.compiler.front.Expression;
import org.manifold.compiler.front.FunctionInvocationExpression;
import org.manifold.compiler.front.FunctionTypeValue;
import org.manifold.compiler.front.LiteralExpression;
import org.manifold.compiler.front.MultipleAssignmentException;
import org.manifold.compiler.front.MultipleDefinitionException;
import org.manifold.compiler.front.PrimitiveFunctionValue;
import org.manifold.compiler.front.Scope;
import org.manifold.compiler.front.TupleTypeValue;
import org.manifold.compiler.front.TupleValue;
import org.manifold.compiler.front.VariableAssignmentExpression;
import org.manifold.compiler.front.VariableIdentifier;
import org.manifold.compiler.front.VariableNotDefinedException;
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
    
    System.out.println("expressions:");
    System.out.print(expressions);
    System.out.println();
    
    Scope toplevel = new Scope();
    
    // mock-up: digital circuits primitives
    // (to be removed when core library and namespaces are implemented)
    createDigitalPrimitives(toplevel);
    
    // Build top-level scope
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
          Expression idType = new LiteralExpression(rvalue.getType(toplevel)); 
          toplevel.defineVariable(identifier, idType);
          toplevel.assignVariable(identifier, rvalue);
        } else {
          assert(false);
        }
      }
    }
    
    System.out.println("top-level identifiers:");
    for (VariableIdentifier id : toplevel.getSymbolIdentifiers()) {
      System.out.println(id);
    }
    
  }
  
  private static void createDigitalPrimitives(Scope scope) 
      throws MultipleDefinitionException, VariableNotDefinedException, 
      MultipleAssignmentException {
    // inputPin: unit -> Bool
    // outputPin: Bool -> unit
    // and: (Bool, Bool) -> Bool
    // or: (Bool, Bool) -> Bool
    // not: Bool -> Bool
    FunctionTypeValue notPrimitiveType = new FunctionTypeValue(
        BooleanTypeValue.getInstance(), BooleanTypeValue.getInstance());
    PrimitiveFunctionValue notPrimitive = new PrimitiveFunctionValue(
        "not", notPrimitiveType);
    VariableIdentifier notIdentifier = new VariableIdentifier(
        Arrays.asList(new String[]{"not"}));
    scope.defineVariable(notIdentifier, 
        new LiteralExpression(notPrimitiveType));
    scope.assignVariable(notIdentifier, new LiteralExpression(notPrimitive));
  }
  
}

class ExpressionVisitor extends ManifoldBaseVisitor<Expression> {
  
  @Override
  public Expression visitAssignmentExpression(
      ManifoldParser.AssignmentExpressionContext context) {
    return new VariableAssignmentExpression(
        visit(context.expression(0)),
        visit(context.expression(1))
    );
  }
  
  @Override
  public Expression visitFunctionInvocationExpression(
      ManifoldParser.FunctionInvocationExpressionContext context) {
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
