package org.manifold.compiler.front;

import org.manifold.compiler.SchematicValueVisitor;
import org.manifold.compiler.TypeValue;
import org.manifold.compiler.UndefinedBehaviourError;

public class ImportTypeValue extends TypeValue implements NamedEntryTypeValue {

  private NamespaceIdentifier namespace;
  private ExpressionGraph exprGraph;

  public ImportTypeValue(ExpressionGraph exprGraph, NamespaceIdentifier namespace) {
    this.exprGraph = exprGraph;
    this.namespace = namespace;
  }

  public TypeValue getEntry(String key) throws Exception {
    VariableIdentifier id = new VariableIdentifier(namespace, key);
    VariableReferenceVertex source = exprGraph.getVariableVertex(id);
    source.elaborate();
    return source.getType();
  }

  @Override
  public void accept(SchematicValueVisitor schematicValueVisitor) {
    throw new UndefinedBehaviourError(
        "cannot accept non-frontend ValueVisitor into a frontend Value");
  }
}
