package org.manifold.compiler.front;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.manifold.compiler.UndefinedBehaviourError;


public class ExpressionGraphBuilder implements ExpressionVisitor {

  private static Logger log = LogManager.getLogger("ExpressionGraph");

  private List<Expression> expressions;
  private Map<NamespaceIdentifier, Namespace> namespaces;

  public ExpressionGraphBuilder(List<Expression> exprs,
      Map<NamespaceIdentifier, Namespace> namespaces) {
    this.expressions = exprs;
    this.namespaces = namespaces;
  }

  private ExpressionGraph exprGraph;

  // visitor state
  private ExpressionVertex lastVertex;

  class GraphConstructionError extends Error {
    private static final long serialVersionUID = -4064007628180935285L;
    private final Throwable cause;
    @Override
    public Throwable getCause() {
      return cause;
    }
    public GraphConstructionError(Throwable cause) {
      this.cause = cause;
    }
    @Override
    public String getMessage() {
      return cause.getMessage();
    }
  }

  // Builds and returns an expression graph for every expression
  // in the default namespace, along with any variables in other namespaces
  // that were referenced from the default one.
  public ExpressionGraph build() throws MultipleDefinitionException {
    NamespaceIdentifier defaultNamespaceID = new NamespaceIdentifier("");
    Namespace defaultNamespace = namespaces.get(defaultNamespaceID);
    // TODO there needs to be a step before this one that resolves
    // all variable references to fully-qualified namespace identifiers
    // (namespace + identifier). for now it is simpler
    // to ignore this because absolutely everything goes into
    // the default namespace at the moment

    exprGraph = new ExpressionGraph();

    // create vertices for all variables first
    for (Map.Entry<NamespaceIdentifier, Namespace> e : namespaces.entrySet()) {
      NamespaceIdentifier nsID = e.getKey();
      log.debug("constructing vertices for variables in namespace "
          + "'" + nsID + "'");
      Namespace ns = e.getValue();
      createVariablesFromScope(nsID, ns.getPublicScope());
      createVariablesFromScope(nsID, ns.getPrivateScope());
    }

    for (Expression expr : expressions) {
      expr.accept(this);
    }

    return exprGraph;
  }

  private void createVariablesFromScope(NamespaceIdentifier nsID,
      Scope scope) throws MultipleDefinitionException {
    for (VariableIdentifier vID : scope.getSymbolIdentifiers()) {
      // scopes don't know their own namespaces, which is why we need nsID here
      VariableIdentifier qualifiedID = new VariableIdentifier(
          nsID, vID.getName());
      log.debug("constructing vertex for '" + qualifiedID + "'");
      exprGraph.createVariableVertex(qualifiedID);
    }
  }

  @Override
  public void visit(FunctionInvocationExpression fExpr) {
    throw new UndefinedBehaviourError("don't know how to visit "
        + "function invocation expression");
  }

  @Override
  public void visit(LiteralExpression lExpr) {
    ConstantValueVertex v = new ConstantValueVertex(lExpr.getValue());
    exprGraph.addNonVariableVertex(v);
    this.lastVertex = v;
  }

  @Override
  public void visit(TupleTypeValueExpression tExpr) {
    Map<String, ExpressionEdge> typeValueEdges = new HashMap<>();
    for (Map.Entry<String, Expression> e 
        : tExpr.getTypeValueExpressions().entrySet()) {
      String identifier = e.getKey();
      e.getValue().accept(this);
      ExpressionEdge eTypeValue = new ExpressionEdge(lastVertex, null);
      typeValueEdges.put(identifier, eTypeValue);
      exprGraph.addEdge(eTypeValue);
    }
    Map<String, ExpressionEdge> defaultValueEdges = new HashMap<>();
    for (Map.Entry<String, Expression> e 
        : tExpr.getDefaultValueExpressions().entrySet()) {
      String identifier = e.getKey();
      e.getValue().accept(this);
      ExpressionEdge eDefaultValue = new ExpressionEdge(lastVertex, null);
      defaultValueEdges.put(identifier, eDefaultValue);
      exprGraph.addEdge(eDefaultValue);
    }
    TupleTypeValueVertex vTuple = new TupleTypeValueVertex(
        typeValueEdges, defaultValueEdges);
    exprGraph.addNonVariableVertex(vTuple);
    this.lastVertex = vTuple;
  }
  
  @Override
  public void visit(FunctionTypeValueExpression fExpr) {
    // get the vertex corresponding to the input type
    fExpr.getInputTypeExpression().accept(this);
    ExpressionVertex vIn = lastVertex;
    ExpressionEdge eIn = new ExpressionEdge(vIn, null);
    // then get the output type vertex
    fExpr.getOutputTypeExpression().accept(this);
    ExpressionVertex vOut = lastVertex;
    ExpressionEdge eOut = new ExpressionEdge(vOut, null);
    
    FunctionTypeValueVertex vFunctionType = new FunctionTypeValueVertex(
        fExpr, eIn, eOut);
    exprGraph.addNonVariableVertex(vFunctionType);
    exprGraph.addEdge(eIn);
    exprGraph.addEdge(eOut);
    this.lastVertex =  vFunctionType;
  }
  
  @Override
  public void visit(VariableAssignmentExpression vExpr) {
    // get the vertex corresponding to the lvalue
    vExpr.getLvalueExpression().accept(this);
    ExpressionVertex vLeft = lastVertex;
    // then get the rvalue...
    vExpr.getRvalueExpression().accept(this);
    ExpressionVertex vRight = lastVertex;
    ExpressionEdge e = new ExpressionEdge(vRight, vLeft);
    exprGraph.addEdge(e);
    // leave vRight in lastVertex
  }

  @Override
  public void visit(VariableReferenceExpression vExpr) {
    // since we have already constructed all variable reference vertices,
    // this always exists (assuming vExpr's identifier is fully qualified)
    try {
      lastVertex = exprGraph.getVariableVertex(vExpr.getIdentifier());
    } catch (VariableNotDefinedException e) {
      throw new GraphConstructionError(e);
    }
  }

  @Override
  public void visit(
      PrimitivePortDefinitionExpression pExpr) {
    pExpr.getTypeValueExpression().accept(this);
    ExpressionVertex vSignalType = lastVertex;
    ExpressionEdge eSignalType = new ExpressionEdge(vSignalType, null);
    exprGraph.addEdge(eSignalType);

    pExpr.getAttributesExpression().accept(this);
    ExpressionVertex vAttributes = lastVertex;
    ExpressionEdge eAttributes = new ExpressionEdge(vAttributes, null);
    exprGraph.addEdge(eAttributes);
    PrimitivePortVertex vPort = new PrimitivePortVertex(
        pExpr, eSignalType, eAttributes);
    exprGraph.addNonVariableVertex(vPort);
    this.lastVertex = vPort;
  }

  @Override
  public void visit(
      PrimitiveNodeDefinitionExpression nExpr) {
    nExpr.getTypeValueExpression().accept(this);
    ExpressionVertex vPortType = lastVertex;
    ExpressionEdge ePortType = new ExpressionEdge(vPortType, null);
    exprGraph.addEdge(ePortType);

    nExpr.getAttributesExpression().accept(this);
    ExpressionVertex vAttributes = lastVertex;
    ExpressionEdge eAttributes = new ExpressionEdge(vAttributes, null);
    exprGraph.addEdge(eAttributes);
    PrimitiveNodeVertex vNode = new PrimitiveNodeVertex(
        nExpr, ePortType, eAttributes);
    exprGraph.addNonVariableVertex(vNode);
    this.lastVertex = vNode;
  }

}
