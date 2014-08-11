package org.manifold.compiler.front;

import org.manifold.compiler.TypeValue;

public class FunctionTypeValue extends TypeValue {
  
  private final TypeValue inputType;
  private final TypeValue outputType;

  public FunctionTypeValue(TypeValue inputType, TypeValue outputType) {
    this.inputType = inputType;
    this.outputType = outputType;
  }

  public TypeValue getInputType() {
    return inputType;
  }

  public TypeValue getOutputType() {
    return outputType;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    } else if (o == null || !(o instanceof FunctionTypeValue)) {
      return false;
    }
    FunctionTypeValue other = (FunctionTypeValue) o;
    return inputType.equals(other.inputType) &&
        outputType.equals(other.outputType);
  }

  @Override
  public boolean isSubtypeOf(TypeValue other) {
    if (this == other) {
      return true;
    }
    if (other == null) {
      return false;
    }
    if (!(other instanceof FunctionTypeValue)) {
      return getSupertype().isSubtypeOf(other);
    }
    FunctionTypeValue oFn = (FunctionTypeValue) other;
    // One way: a function is a subtype of oFn if it can be used where oFn
    // can be used.
    // Must accept all inputs of oFn and outputs must be subtypes
    // of outputs of oFn.
    return oFn.getInputType().isSubtypeOf(getInputType()) &&
        getOutputType().isSubtypeOf(oFn);
  }
}
