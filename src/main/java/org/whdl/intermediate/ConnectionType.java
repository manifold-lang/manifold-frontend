package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


public class ConnectionType extends Type {
  private Map<String, UserDefinedType> attributes;
  
  public ConnectionType(String typename){
    super(typename);
    this.attributes = new HashMap<String, UserDefinedType>();
  }

  @Override
  public Value instantiate() {
    Connection con = new Connection(this);
    // elaborate default attributes
    for(Entry<String, UserDefinedType> attr : attributes.entrySet()){
      String attrName = attr.getKey();
      UserDefinedType attrTypeValue = attr.getValue();
      Value attrValue = attrTypeValue.instantiate();
      con.setAttribute(attrName, attrValue);
    }
    return con;
  }
  
  public void addAttribute(String attrName, UserDefinedType attrType) {
    // FIXME multiple additions of the same attribute?
    attributes.put(attrName, attrType);
  }
}
