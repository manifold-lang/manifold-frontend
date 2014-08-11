package org.manifold.compiler.front;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.manifold.compiler.BooleanTypeValue;
import org.manifold.compiler.Main;
import org.manifold.compiler.NilTypeValue;
import org.manifold.compiler.TypeValue;
import org.manifold.compiler.UndeclaredIdentifierException;
import org.manifold.compiler.middle.Schematic;

import com.google.common.collect.ImmutableList;

public class TestMain {
  private static final TypeValue nilType = NilTypeValue.getInstance();
  private static final TypeValue boolType = BooleanTypeValue.getInstance();

  // TODO: Remove and replace with tests of the digital logic module.
  @Test
  public void testCreateDigitalPrimitives() throws MultipleDefinitionException,
      VariableNotDefinedException, MultipleAssignmentException,
      TypeMismatchException, UndeclaredIdentifierException, 
      org.manifold.compiler.MultipleDefinitionException {
    Scope toplevel = new Scope();
    Schematic schematic = new Schematic("test");
    Main.createDigitalPrimitives(toplevel, schematic);
    VariableIdentifier varId = new VariableIdentifier(
        ImmutableList.of("inputPin"));
    TypeValue inputPinType = new FunctionTypeValue(nilType, boolType);
    assertEquals(inputPinType, toplevel.getVariableType(varId));
  }

}
