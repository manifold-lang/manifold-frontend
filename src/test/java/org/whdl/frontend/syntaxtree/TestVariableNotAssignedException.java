package org.whdl.frontend.syntaxtree;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class TestVariableNotAssignedException {

  private NamespaceIdentifier namespaceIdentifierInstance;

  private NamespaceIdentifier getNamespaceIdentifierInstance() {
    if (namespaceIdentifierInstance == null) {
      ArrayList<String> name = new ArrayList<String>(1);
      name.add("whdl");

      namespaceIdentifierInstance = new NamespaceIdentifier(name);
    }

    return namespaceIdentifierInstance;
  }

  private VariableIdentifier getVariableIdentifierInstance() {
    return new VariableIdentifier(getNamespaceIdentifierInstance(), "foo");
  }
  
  // because we just need something that returns a type, without bringing too
  // much else into the works
  // FIXME(lucas) Remove this once we have an actual LiteralExpression
  static private class FacadeExpression extends Expression {
    
    private Value value;
 
    public FacadeExpression(Value value) {
      this.value = value;
    }

    @Override
    public Value evaluate() { return value; }

    @Override
    public TypeValue getType() { return value.getType(); }
 
  }

  private Expression getTypeExpression(){
    return new FacadeExpression(BitTypeValue.getInstance());
  }
  
  private Variable v_inst = null; 
  public Variable getVariableInstance(){
    if(v_inst == null){
      v_inst = new Variable(getVariableIdentifierInstance(), getTypeExpression());
    }
    return v_inst;
  }
  
  public VariableNotAssignedException getInstance(){
    return new VariableNotAssignedException(getVariableInstance());
  }
  
  @Test
  public void testGetMessage_containsVariableIdentifier() {
    VariableNotAssignedException instance = getInstance();
    String message = instance.getMessage();
    assertTrue(message.contains(getVariableInstance().getIdentifier().toString()));
  }

}
