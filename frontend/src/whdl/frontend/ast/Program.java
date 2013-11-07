package whdl.frontend.ast;

public class Program {
	private Identifier namespaceID;
	private PackageList packageList;
	private StatementList statements;
	public Program(Identifier namespaceID, PackageList packageList, StatementList statements){
		this.namespaceID = namespaceID;
		this.packageList = packageList;
		this.statements = statements;
	}
	public Identifier getNamespaceID(){return namespaceID;}
	public PackageList getPackageList(){return packageList;}
	public StatementList getStatements(){return statements;}
}
