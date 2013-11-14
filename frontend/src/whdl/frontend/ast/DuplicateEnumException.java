package whdl.frontend.ast;

public class DuplicateEnumExpression extends TranslationException {

	private static final long serialVersionUID = 3556820750098313861L;
	
	private Identifier duplicateID;
	public DuplicateEnumExpression(Identifier duplicateID){
		this.duplicateID = duplicateID;
	}
	@Override
	public String getMessage(){
		return "duplicate field '" + duplicateID.toString() + "' in tuple declaration";
	}
}
