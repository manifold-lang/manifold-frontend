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

}
