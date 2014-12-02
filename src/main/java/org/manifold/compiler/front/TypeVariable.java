package org.manifold.compiler.front;

import org.manifold.compiler.SchematicValueVisitor;
import org.manifold.compiler.TypeValue;

public class TypeVariable extends TypeValue {

  private static int nextID = 0;
  private synchronized int allocateNewID() {
    int id = nextID;
    nextID += 1;
    return id;
  }

  private final int id;
  public Integer getID() {
    return id;
  }

  public TypeVariable() {
    this.id = allocateNewID();
  }

  private String name = null;

  @Override
  public String toString() {
    if (instance != null) {
      return instance.toString();
    } else {
      if (name == null) {
        // generate a new name based on the ID
        String tmp = "";
        int tmpID = id;
        while (tmpID >= 26) {
          tmp += (char) (97 + tmpID % 26);
          tmpID /= 26;
        }
        tmp += (char) (97 + tmpID);
        name = tmp;
      }
      return name;
    }
  }

  private TypeValue instance = null;
  public TypeValue getInstance() {
    return instance;
  }
  public void setInstance(TypeValue inst) {
    this.instance = inst;
  }

  @Override
  public void accept(SchematicValueVisitor v) {
    throw new UnsupportedOperationException("attempted to visit type variable");
  }

  @Override
  public boolean equals(Object aThat) {
    if (this == aThat) {
      return true;
    } else if (!(aThat instanceof TypeVariable)) {
      return false;
    } else {
      TypeVariable that = (TypeVariable) aThat;
      return (this.getID().equals(that.getID()));
    }
  }

  @Override
  public int hashCode() {
    return getID();
  }

}
