package org.whdl.frontend;

public abstract class TypeValue extends Value {

 /*
  * Java doesn't let us express this but every `TypeValue`, by convention,
  * ought to have a `public abstract final` singleton `instance` property
  *
  *   public abstract final TypeValue instance;
  *
  * and a private constructor
  *
  *   private abstract TypeValue();
  */

  public TypeValue getTypeValue() {
    return TypeTypeValue.instance;
  }

}