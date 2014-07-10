package org.manifold.compiler.front;

public class Namespace {
  private NamespaceIdentifier absoluteName;
  private Scope privateScope = new Scope();
  private Scope publicScope = new Scope();

  public Namespace(NamespaceIdentifier absoluteName) {
    this.absoluteName = absoluteName;
  }

  public NamespaceIdentifier getAbsoluteName() {
    return absoluteName;
  }

  public Scope getPrivateScope() {
    return privateScope;
  }

  public Scope getPublicScope() {
    // TODO we may or may not want to make this immutable in the future,
    // depending on where this is populated
    return publicScope;
  }

  // TODO(murphy)
  // public Set<CompilationUnit> getCompilationUnits();

}
