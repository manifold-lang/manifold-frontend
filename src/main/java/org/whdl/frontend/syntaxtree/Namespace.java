package org.whdl.frontend.syntaxtree;

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
    return publicScope;
  }

  // TODO(murphy)
  // public Set<CompilationUnit> getFiles();

  public Variable getVariable(VariableIdentifier identifier)
      throws VariableNotDefinedException {
    // can only search public identifiers
    return publicScope.getVariable(identifier);
  }

}
