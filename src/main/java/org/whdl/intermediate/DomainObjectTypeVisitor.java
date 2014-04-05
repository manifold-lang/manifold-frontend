package org.whdl.intermediate;

import org.whdl.intermediate.types.*;

public class DomainObjectTypeVisitor {

  public Type visit(BooleanLiteral e){
    return PrimitiveType.BOOLEAN;
  }
  
  public Type visit(IntegerLiteral e){
    return PrimitiveType.INTEGER;
  }
  
  public Type visit(StringLiteral e){
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
