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
  public Value instantiate() {
    Connection con = new Connection(this);
    // elaborate default attributes
    for(Entry<String, TypeTypeDefinition> attr : attributes.entrySet()){
      String attrName = attr.getKey();
      TypeTypeDefinition attrTypeValue = attr.getValue();
      Value attrValue = attrTypeValue.instantiate();
      con.setAttribute(attrName, attrValue);
    }
    return con;
  }
  
  public void addAttribute(String attrName, TypeTypeDefinition attrType) {
    // FIXME multiple additions of the same attribute?
    attributes.put(attrName, attrType);
  }
  
}
