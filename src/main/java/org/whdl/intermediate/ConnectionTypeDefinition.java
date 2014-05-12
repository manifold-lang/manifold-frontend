package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ConnectionTypeDefinition extends TypeDefinition {
  private Map<String, TypeTypeDefinition> attributes;
  
  public ConnectionTypeDefinition(String typename){
    super(typename);
    this.attributes = new HashMap<String, TypeTypeDefinition>();
  }

  @Override
  public Value instantiate(String instanceName) {
    Connection con = new Connection(instanceName, this);
    // elaborate default attributes
    for(Entry<String, TypeTypeDefinition> attr : attributes.entrySet()){
      String attrName = attr.getKey();
      TypeTypeDefinition attrTypeValue = attr.getValue();
      Value attrValue = attrTypeValue.instantiate(attrName);
      con.setAttribute(attrName, attrValue);
    }
    return con;
  }
  
}
