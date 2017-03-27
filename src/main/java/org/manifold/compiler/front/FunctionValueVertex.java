package org.manifold.compiler.front;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.manifold.compiler.TypeTypeValue;
import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

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
    this.functionTypeEdge.setName("type");
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

  @Override
  public void elaborate() throws Exception {
    if (function != null) {
      return;
    }
    log.debug("elaborating function definition");
    ExpressionVertex typeVertex = functionTypeEdge.getSource();
    typeVertex.elaborate();
    if (!(typeVertex.getValue() instanceof FunctionTypeValue)) {
      TypeValue typeConstructor = new FunctionTypeValue(
          TypeTypeValue.getInstance(), TypeTypeValue.getInstance());
      throw new TypeMismatchException(
          typeConstructor, typeVertex.getValue());
    }
    type = (FunctionTypeValue) typeVertex.getValue();
    // inject input/output TupleValues
    TupleTypeValue inputType = (TupleTypeValue) type.getInputType();
    TupleTypeValue outputType = (TupleTypeValue) type.getOutputType();
    MappedArray<String, ExpressionEdge> inputEdges = new MappedArray<>();
    MappedArray<String, ExpressionEdge> outputEdges = new MappedArray<>();
    inputType.getSubtypes().forEach((argName) -> {
      ExpressionEdge e = new ExpressionEdge(null, null); // I know what I'm doing
      functionBody.addEdge(e);
      inputEdges.put(argName.getKey(), e);
    });
    outputType.getSubtypes().forEach((typeEntry) -> {
      String argName = typeEntry.getKey();
      // "name resolution": look for a variable reference vertex with this name in the subgraph
      for (Map.Entry<VariableIdentifier, VariableReferenceVertex> vRef
          : functionBody.getVariableVertices().entrySet()) {
        if (vRef.getKey() == null) {
          // then the user tried to have an output tuple without named keys
          throw new FrontendBuildException("The function output type is an anonymous tuple. " +
              "Give the anonymous value a key to fix this.");
        }
        if (vRef.getKey().getName().equals(argName)) {
          ExpressionEdge e = new ExpressionEdge(vRef.getValue(), null);
          functionBody.addEdge(e);
          outputEdges.put(argName, e);
          break;
        }
      }
    });
    TupleValueVertex vInput = new TupleValueVertex(functionBody, inputEdges);
    functionBody.addVertex(vInput);
    TupleValueVertex vOutput = new TupleValueVertex(functionBody, outputEdges);
    functionBody.addVertex(vOutput);
    function = new FunctionValue(type, functionBody, vInput, vOutput);
    // inject StaticAttributeAccesses to assign each variable from the input
    // TODO we're making a few guesses about "name resolution" here
    for (Map.Entry<VariableIdentifier, VariableReferenceVertex> entry
          : functionBody.getVariableVertices().entrySet()) {
      String varName = entry.getKey().getName();
      if (inputType.getSubtypes().containsKey(varName)) {
        ExpressionEdge eInput = new ExpressionEdge(vInput, null);
        functionBody.addEdge(eInput);
        StaticAttributeAccessVertex vAccess = new StaticStringAttributeAccessVertex(
            functionBody, eInput, varName);
        functionBody.addVertex(vAccess);
        ExpressionEdge eAssign = new ExpressionEdge(vAccess, entry.getValue());
        functionBody.addEdge(eAssign);
      }
    }
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
  public String toString() {
    if (function == null) {
      return "function value (not elaborated)";
    } else {
      return "function value " + type.toString();
    }
  }

  @Override
  public void writeToDOTFile(BufferedWriter writer) throws IOException {
    String objectID = Integer.toString(System.identityHashCode(this));
    String label = this.toString();
    writer.write(objectID);
    writer.write(" [");
    writer.write("label=\"");
    writer.write(objectID);
    writer.write("\n");
    writer.write(label);
    writer.write("\"");
    writer.write("];");
    writer.newLine();
  }

  @Override
  public ExpressionVertex copy(ExpressionGraph g,
      Map<ExpressionEdge, ExpressionEdge> edgeMap) {
    // Use a new expression graph for the body, just like when constructing a function initially.
    // This maintains proper scoping
    ExpressionGraph newBody = new ExpressionGraph();
    newBody.addSubGraph(functionBody);

    return new FunctionValueVertex(g, edgeMap.get(functionTypeEdge), newBody);
  }
}

