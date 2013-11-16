package org.whdl.frontend.syntaxtree;

import java.util.List;

public class CompilationUnit {
	private NamespaceIdentifier namespace;
	private List<NamespaceIdentifier> imports;
	private List<Statement> statements;
	private Scope fileScope;
	
	public CompilationUnit(NamespaceIdentifier namespace, List<NamespaceIdentifier> imports, List<Statement> statements){
		this.namespace = namespace;
		this.imports = imports;
		this.statements = statements;
		this.fileScope = new Scope();
	}
	
	public NamespaceIdentifier getNamespace(){return namespace;}
	public List<NamespaceIdentifier> getImports(){return imports;}
	public List<Statement> getStatements(){return statements;}
	
	public void populateScope(Scope s){
		// FIXME implement this
	}
	
}
