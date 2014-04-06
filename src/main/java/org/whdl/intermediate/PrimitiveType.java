package org.whdl.intermediate;


public class PrimitiveType extends Type{
  public enum PrimitiveKind {
    BOOLEAN,
    INTEGER,
    STRING,
  }
  
  private PrimitiveKind kind;
  
  public PrimitiveType(PrimitiveKind kind){
    this.kind = kind;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((kind == null) ? 0 : kind.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    PrimitiveType other = (PrimitiveType) obj;
    if (kind != other.kind) {
      return false;
    }
    return true;
  }
  
  
}
