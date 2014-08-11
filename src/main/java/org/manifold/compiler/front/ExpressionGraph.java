package org.manifold.compiler.front;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.manifold.compiler.ArrayTypeValue;
import org.manifold.compiler.ArrayValue;
import org.manifold.compiler.BooleanTypeValue;
import org.manifold.compiler.BooleanValue;
import org.manifold.compiler.ConnectionType;
import org.manifold.compiler.ConnectionValue;
import org.manifold.compiler.ConstraintType;
import org.manifold.compiler.ConstraintValue;
import org.manifold.compiler.IntegerTypeValue;
import org.manifold.compiler.IntegerValue;
import org.manifold.compiler.NilTypeValue;
import org.manifold.compiler.NodeTypeValue;
import org.manifold.compiler.NodeValue;
import org.manifold.compiler.PortTypeValue;
import org.manifold.compiler.PortValue;
import org.manifold.compiler.StringTypeValue;
import org.manifold.compiler.StringValue;
import org.manifold.compiler.TypeTypeValue;
import org.manifold.compiler.UndefinedBehaviourError;
import org.manifold.compiler.Value;
import org.manifold.compiler.ValueVisitor;

public class ExpressionGraph implements ExpressionVisitor, ValueVisitor {
  private List<PrimitiveFunctionVertex> primitiveFunctionVertices =
      new ArrayList<>();
  private Map<VariableIdentifier, VariableReferenceVertex> variableVertices = 
      new HashMap<>();
  private List<ExpressionEdge> edges = new ArrayList<>();
  
  public List<ExpressionEdge> getEdgesFromSource(ExpressionVertex v) {
    List<ExpressionEdge> edgesFrom = new LinkedList<>();
    for (ExpressionEdge e : edges) {
      if(v.equals(e.getSource())) {
        edgesFrom.add(e);
      }
    }
    return edgesFrom;
  }
  
  public List<ExpressionEdge> getEdgesToTarget(ExpressionVertex v) {
    List<ExpressionEdge> edgesTo = new LinkedList<>();
    for (ExpressionEdge e : edges) {
      if(v.equals(e.getTarget())) {
        edgesTo.add(e);
      }
    }
    return edgesTo;
  }
  
  public List<String> getPrintableEdges() {
    List<String> edgeList = new LinkedList<>();
    for (ExpressionEdge e : edges) {
      edgeList.add(e.toString());
    }
    return edgeList;
  }
  
  private Scope scope;
  
  public ExpressionGraph(Scope scope) {
    this.scope = scope;
  }
  
  public void buildFrom(List<Expression> expressions) {
    for (Expression e : expressions) {
      e.accept(this);
    }
  }

  // edge leading from the last expression we visited
  private ExpressionEdge lastSourceEdge = null;
  
  @Override
  public void visit(FunctionInvocationExpression functionInvocationExpression) {
    Expression funcExpr = functionInvocationExpression.getFunctionExpression();
    Expression inputExpr = functionInvocationExpression.getInputExpression();
    // first, visit the input
    inputExpr.accept(this);
    // get the edge coming from the input
    ExpressionEdge inputEdge = lastSourceEdge;
    // evaluate the expression and visit its value
    Value funcValue = funcExpr.getValue(scope);
    funcValue.accept(this);
    // get the edge coming from the function
    ExpressionEdge funcEdge = lastSourceEdge;
    // rewrite to connect input --inputEdge-> func
    ExpressionVertex func = funcEdge.getSource();
    inputEdge.setTarget(func);
    lastSourceEdge = funcEdge;
  }

  @Override
  public void visit(LiteralExpression literalExpression) {
    Value v = literalExpression.getValue(scope);
    v.accept(this);
    // TODO perhaps use a ValueVisitor instead of this? for demo purposes only
    if (v instanceof PrimitiveFunctionValue) {
      
    } else if (v instanceof TupleValue) {
      
    } else {
      throw new UndefinedBehaviourError(
          "unhandled LiteralExpression value " + v.getClass());
    }
  }

  @Override
  public void visit(VariableAssignmentExpression variableAssignmentExpression) {
    Expression lvalue = variableAssignmentExpression.getLvalueExpression();
    Expression rvalue = variableAssignmentExpression.getRvalueExpression();
    // first, visit the source
    rvalue.accept(this);
    // get the edge coming from the source
    ExpressionEdge sourceEdge = lastSourceEdge;
    // then visit the target
    lvalue.accept(this);
    // get the edge coming from the target (so that we can find the target)
    ExpressionEdge targetEdge = lastSourceEdge;
    // rewrite to connect source --sourceEdge-> target
    ExpressionVertex target = targetEdge.getSource();
    sourceEdge.setTarget(target);
    lastSourceEdge = targetEdge; // to support "a = b = c;"
  }

  @Override
  public void visit(VariableReferenceExpression variableReferenceExpression) {
    VariableIdentifier id = variableReferenceExpression.getIdentifier();
    VariableReferenceVertex source;
    if (!variableVertices.containsKey(id)) {
      source = new VariableReferenceVertex(id);
      variableVertices.put(id, source);
    } else {
      source = variableVertices.get(id);
    }
    ExpressionEdge edgeVariableOut = new ExpressionEdge(source, null);
    edges.add(edgeVariableOut);
    lastSourceEdge = edgeVariableOut;
  }

  @Override
  public void visit(ArrayTypeValue arrayTypeValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(PrimitiveFunctionValue pFunc) {
    PrimitiveFunctionVertex pVertex = new PrimitiveFunctionVertex(pFunc);
    primitiveFunctionVertices.add(pVertex);
    ExpressionEdge edgeFunctionOut = new ExpressionEdge(pVertex, null);
    edges.add(edgeFunctionOut);
    lastSourceEdge = edgeFunctionOut;
  }

  @Override
  public void visit(TupleValue tuple) {
    List<ExpressionEdge> valueEdges = new ArrayList<ExpressionEdge>();
    for (int i = 0; i < tuple.getSize(); ++i) {
      Expression e = tuple.entry(i);
      e.accept(this);
      ExpressionEdge valueEdge = lastSourceEdge;
      valueEdges.add(valueEdge);
    }
    TupleValueVertex tupleVertex = new TupleValueVertex(tuple, valueEdges);
    ExpressionEdge edgeTupleOut = new ExpressionEdge(tupleVertex, null);
    edges.add(edgeTupleOut);
    lastSourceEdge = edgeTupleOut;
  }

  @Override
  public void visit(TupleTypeValue tupleTypeValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(FunctionValue functionValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(FunctionTypeValue functionTypeValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(EnumValue enumValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(EnumTypeValue enumTypeValue) {
    throw new UnsupportedOperationException("illegal value"); 
  }

  @Override
  public void visit(TypeTypeValue typeTypeValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(StringValue stringValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(StringTypeValue stringTypeValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(PortValue portValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(PortTypeValue portTypeValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(NodeValue nodeValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(NodeTypeValue nodeTypeValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(NilTypeValue nilTypeValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(IntegerValue integerValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(ConstraintValue constraintValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(IntegerTypeValue integerTypeValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(ConstraintType constraintType) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(ConnectionValue connectionValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(ConnectionType connectionType) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(BooleanValue booleanValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(BooleanTypeValue booleanTypeValue) {
    throw new UnsupportedOperationException("illegal value");
  }

  @Override
  public void visit(ArrayValue arrayValue) {
    throw new UnsupportedOperationException("illegal value");
  }
  
}
