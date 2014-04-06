package org.whdl.intermediate;

import org.whdl.intermediate.types.*;

public class ValueTypeVisitor {

  public Type visit(BooleanValue e){
    return PrimitiveType.BOOLEAN;
  }
  
  public Type visit(IntegerValue e){
    return PrimitiveType.INTEGER;
  }
  
  public Type visit(StringValue e){
    return PrimitiveType.STRING;
  }

  public Type visit(Node node) {
    String defName = node.getDefinition().getTypename();
    return new NodeType(defName);
  }

  public Type visit(Endpoint endpoint) {
    String defName = endpoint.getDefinition().getTypename();
    return new EndpointType(defName);
  }

  public Type visit(Connection connection) {
    String defName = connection.getDefinition().getTypename();
    return new ConnectionType(defName);
  }

  public Type visit(Constraint constraint) {
    String defName = constraint.getDefinition().getTypename();
    return new ConstraintType(defName);
  }

}
