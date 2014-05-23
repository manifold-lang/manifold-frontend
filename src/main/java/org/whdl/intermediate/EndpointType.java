package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


public class EndpointType extends Type {
  private Map<String, UserDefinedType> attributes;
  
  public EndpointType(){
    this.attributes = new HashMap<String, UserDefinedType>();
  }
  
  @Override
  public Value instantiate() throws TypeMismatchException {
    Endpoint ept = new Endpoint(this);
    // elaborate default attributes
    for(Entry<String, UserDefinedType> attr : attributes.entrySet()){
      String attrName = attr.getKey();
      UserDefinedType attrTypeValue = attr.getValue();
      Value attrValue = attrTypeValue.instantiate();
      ept.setAttribute(attrName, attrValue);
    }
    return ept;
  }

  public void addAttribute(String attrName, UserDefinedType attrType) {
    // FIXME multiple additions of the same attribute?
    attributes.put(attrName, attrType);
  }
  
}
