package whdl.frontend.ast;

import java.util.List;
import java.util.LinkedList;

public class PackageList {
	private List<Identifier> packages;
	public PackageList(){
		this.packages = new LinkedList<Identifier>();
	}
	public List<Identifier> getPackages(){return packages;}
	public void add(Identifier s){
		packages.add(s);
	}
	public int size(){
		return packages.size();
	}
	public Identifier get(int i){
		return packages.get(i);
	}
}
