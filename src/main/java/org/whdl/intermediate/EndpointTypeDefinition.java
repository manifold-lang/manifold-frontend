package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class EndpointTypeDefinition extends TypeDefinition {
  private Map<String, TypeTypeDefinition> attributes;
  
  public EndpointTypeDefinition(String typename){
    super(typename);
    this.attributes = new HashMap<String, TypeTypeDefinition>();
  }
  
  @Override
  public Value instantiate() {
    Endpoint ept = new Endpoint(this);
    // elaborate default attributes
    for(Entry<String, TypeTypeDefinition> attr : attributes.entrySet()){
      String attrName = attr.getKey();
      TypeTypeDefinition attrTypeValue = attr.getValue();
      Value attrValue = attrTypeValue.instantiate();
      ept.setAttribute(attrName, attrValue);
    }
    return ept;
  }

  public void addAttribute(String attrName, TypeTypeDefinition attrType) {
    // FIXME multiple additions of the same attribute?
    attributes.put(attrName, attrType);
  }
  
}
