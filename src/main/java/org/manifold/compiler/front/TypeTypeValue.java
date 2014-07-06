package org.manifold.compiler.front;

public class TypeTypeValue extends TypeValue {

  private static final TypeTypeValue instance = new TypeTypeValue();

  public static TypeTypeValue getInstance(){
    return instance;
  }

  private TypeTypeValue() { }

  public TypeTypeValue getType() {
    return instance;
  }

  public void verify() { }

  // We override the isSubtypeOf method to prevent recursive loops.
  @Override
  public boolean isSubtypeOf(TypeValue type) {
    return this == type;
  }
}
