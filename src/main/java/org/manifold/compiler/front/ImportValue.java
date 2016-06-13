package org.manifold.compiler.front;

import org.manifold.compiler.SchematicValueVisitor;
import org.manifold.compiler.Value;

public class ImportValue extends Value implements NamedEntryValue {
  private ExpressionGraph exprGraph;
  private NamespaceIdentifier namespace;

  public ImportValue(ExpressionGraph exprGraph, NamespaceIdentifier namespace) {
    super(new ImportTypeValue(exprGraph, namespace));
    this.exprGraph = exprGraph;
    this.namespace = namespace;
  }
  @Override
  public boolean isElaborationtimeKnowable() {
    return false;
  }

  @Override
  public boolean isRuntimeKnowable() {
    return false;
  }

  @Override
  public void accept(SchematicValueVisitor schematicValueVisitor) {
    return;
  }

  @Override
  public Value getEntry(String key) throws Exception {
    VariableIdentifier id = new VariableIdentifier(namespace, key);
    VariableReferenceVertex source = exprGraph.getVariableVertex(id);
    source.elaborate();
    return source.getValue();
  }
}
