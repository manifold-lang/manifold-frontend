package org.whdl.frontend.syntaxtree;

public class VariableIdentifier {

	private String name;
	private NamespaceIdentifier namespaceIdentifier;

	public VariableIdentifier(NamespaceIdentifier namespaceIdentifier,
			String name) {
		this.name = name;
		this.namespaceIdentifier = namespaceIdentifier;
	}

	public String getName() {
		return name;
	}

	public NamespaceIdentifier getNamespaceIdentifier() {
		return namespaceIdentifier;
	}

	@Override
	public String toString() {
		String nsString = getNamespaceIdentifier().toString();
		if (nsString.isEmpty()) {
			return getName();
		} else {
			// FIXME this is syntax-dependent
			return nsString + "." + getName();
		}
	}

	@Override
	public int hashCode(){
		return 3 + 19 * getName().hashCode() + 37 * getNamespaceIdentifier().hashCode();
	}
	
	@Override
	public boolean equals(Object aThat) {
		if (this == aThat)
			return true;
		if (!(aThat instanceof VariableIdentifier))
			return false;
		VariableIdentifier that = (VariableIdentifier) aThat;
		// two variable identifiers are equal if they have the same namespace and name
		return (this.getName().equals(that.getName()) && 
				this.getNamespaceIdentifier().equals(that.getNamespaceIdentifier()));
	}
}
