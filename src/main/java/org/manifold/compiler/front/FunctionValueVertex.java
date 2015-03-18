package org.manifold.compiler.front;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.manifold.compiler.TypeTypeValue;
import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

public class FunctionValueVertex extends ExpressionVertex {
  
  private static Logger log = LogManager.getLogger(FunctionValueVertex.class);
  
  private final ExpressionEdge functionTypeEdge;
  public ExpressionEdge getFunctionTypeEdge() {
    return this.functionTypeEdge;
  }
  
  private final ExpressionGraph functionBody;
  public ExpressionGraph getFunctionBody() {
    return this.functionBody;
  }
  
  public FunctionValueVertex(ExpressionGraph exprGraph,
      ExpressionEdge functionTypeEdge, ExpressionGraph functionBody) {
    super(exprGraph);
    this.functionTypeEdge = functionTypeEdge;
    this.functionTypeEdge.setTarget(this);
    this.functionBody = functionBody;
  }

  private FunctionTypeValue type = null;
  
  @Override
  public TypeValue getType() {
    // this is a function, so return a function type
    return this.type;
  }

  private FunctionValue function = null;
  @Override
  public Value getValue() {
    return this.function;
  }

  private TupleValueVertex vInput = null;
  public TupleValueVertex getInputVertex() {
    return this.vInput;
  }
  
  private TupleValueVertex vOutput = null;
  public TupleValueVertex getOutputVertex() {
    return this.vOutput;
  }
  
  @Override
  public void elaborate() throws Exception {
    if (function != null) {
      return;
    }
    log.debug("elaborating function definition");
    ExpressionVertex typeVertex = functionTypeEdge.getSource();
    typeVertex.elaborate();
    TypeValue typeConstructor = new FunctionTypeValue(
        TypeTypeValue.getInstance(), TypeTypeValue.getInstance());
    if (!(typeVertex.getValue() instanceof FunctionTypeValue)) {
      throw new TypeMismatchException(
          typeConstructor, typeVertex.getValue());
    }
    type = (FunctionTypeValue) typeVertex.getValue();
    function = new FunctionValue(type, functionBody);
    // inject input/output TupleValues
    TupleTypeValue inputType = (TupleTypeValue) type.getInputType();
    TupleTypeValue outputType = (TupleTypeValue) type.getOutputType();
    Map<String, ExpressionEdge> inputEdges = new HashMap<>();
    Map<String, ExpressionEdge> outputEdges = new HashMap<>();
    for (String argName : inputType.getSubtypes().keySet()) {
      ExpressionEdge e = new ExpressionEdge(null, null); // I know what I'm doing
      functionBody.addEdge(e);
      inputEdges.put(argName, e);
    }
    for (String argName : outputType.getSubtypes().keySet()) {
      // "name resolution": look for a variable reference vertex with this name in the subgraph
      for (Map.Entry<VariableIdentifier, VariableReferenceVertex> vRef 
          : functionBody.getVariableVertices().entrySet()) {
        if (vRef.getKey().getName().equals(argName)) {
          ExpressionEdge e = new ExpressionEdge(vRef.getValue(), null);
          functionBody.addEdge(e);
          outputEdges.put(argName, e);
          break;
        }
      }
    }
    vInput = new TupleValueVertex(functionBody, inputEdges);
    functionBody.addVertex(vInput);
    vOutput = new TupleValueVertex(functionBody, outputEdges);
    functionBody.addVertex(vOutput);
    // TODO inject StaticAttributeAccesses to assign each variable from the input
  }

  @Override
  public void verify() throws Exception {
    // TODO Auto-generated method stub
    
  }

  @Override
  public boolean isElaborationtimeKnowable() {
    return true;
  }

  @Override
  public boolean isRuntimeKnowable() {
    return false;
  }

  @Override
  public void writeToDOTFile(BufferedWriter writer) throws IOException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public ExpressionVertex copy(ExpressionGraph g,
      Map<ExpressionEdge, ExpressionEdge> edgeMap) {
    // TODO Auto-generated method stub
    return null;
  }

}

