package org.manifold.compiler.front;

import java.util.HashMap;
import java.util.Map;

import org.manifold.compiler.BooleanTypeValue;
import org.manifold.compiler.IntegerTypeValue;
import org.manifold.compiler.NilTypeValue;
import org.manifold.compiler.Value;

public class ReservedIdentifiers {
  private static final ReservedIdentifiers instance = new ReservedIdentifiers();
  // Singleton object.
  public static ReservedIdentifiers getInstance() {
    return instance;
  }
  
  private Map<VariableIdentifier, Value> identifiers =
      new HashMap<>();
  
  private ReservedIdentifiers() {
    NamespaceIdentifier defaultNamespace = new NamespaceIdentifier("");
    addIdentifier(new VariableIdentifier(defaultNamespace,
        "Nil"), NilTypeValue.getInstance());
    addIdentifier(new VariableIdentifier(defaultNamespace, 
        "Bool"), BooleanTypeValue.getInstance());
    addIdentifier(new VariableIdentifier(defaultNamespace, 
        "Int"), IntegerTypeValue.getInstance());
  }
  
  public void addIdentifier(VariableIdentifier id, Value v) {
    identifiers.put(id, v);
  }
  
  public boolean isReservedIdentifier(VariableIdentifier id) {
    return identifiers.containsKey(id);
  }
  
  public Value getValue(VariableIdentifier id) {
    if (!isReservedIdentifier(id)) {
      throw new IllegalArgumentException(
          "identifier '" + id + "' is not a reserved id");
    } else {
      return identifiers.get(id);
    }
  }
  
}
