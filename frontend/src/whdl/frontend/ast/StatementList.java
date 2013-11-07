package whdl.frontend.ast;

import java.util.List;
import java.util.LinkedList;

public class StatementList {
	private List<Statement> statements;
	public StatementList(){
		statements = new LinkedList<Statement>();
	}
	public List<Statement> getStatements(){return statements;}
	public void add(Statement s){
		statements.add(s);
	}
	public int size(){
		return statements.size();
	}
	public Statement get(int i){
		return statements.get(i);
	}
}
