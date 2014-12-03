package org.manifold.compiler.front;

import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
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
import org.manifold.compiler.RealTypeValue;
import org.manifold.compiler.RealValue;
import org.manifold.compiler.StringTypeValue;
import org.manifold.compiler.StringValue;
import org.manifold.compiler.TypeTypeValue;
import org.manifold.compiler.UndefinedBehaviourError;
import org.manifold.compiler.UserDefinedTypeValue;


public class ExpressionGraphBuilder
    implements ExpressionVisitor, FrontendValueVisitor {

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
    exprGraph.addConstantValueVertex(v);
    this.lastVertex = v;
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

    ExpressionEdge eAttributes = null;
    pExpr.getAttributesExpression().accept(this);
    ExpressionVertex vAttributes = lastVertex;
    eAttributes = new ExpressionEdge(vAttributes, null);
    exprGraph.addEdge(eAttributes);
    PrimitivePortVertex vPort = new PrimitivePortVertex(
        pExpr, eSignalType, eAttributes);
    exprGraph.addPrimitivePortVertex(vPort);
    this.lastVertex = vPort;
  }

  @Override
  public void visit(ArrayTypeValue arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(TypeTypeValue arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(StringValue arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(StringTypeValue arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(PortValue arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(PortTypeValue arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(NodeValue arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(NodeTypeValue arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(NilTypeValue arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(IntegerValue arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(ConstraintValue arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(IntegerTypeValue arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(ConstraintType arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(ConnectionValue arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(ConnectionType arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(BooleanValue arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(BooleanTypeValue arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(ArrayValue arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(RealTypeValue arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(RealValue arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(UserDefinedTypeValue arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(PrimitiveFunctionValue pFunc) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(TupleValue tuple) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(TupleTypeValue tupleTypeValue) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(FunctionValue functionValue) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(FunctionTypeValue functionTypeValue) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(EnumValue enumValue) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(EnumTypeValue enumTypeValue) {
    // TODO Auto-generated method stub

  }

  @Override
  public void visit(UnknownTypeValue unknownTypeValue) {
    // TODO Auto-generated method stub

  }

}
