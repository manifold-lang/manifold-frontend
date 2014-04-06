package org.whdl.intermediate;

public class ValueTypeVisitor {

  public Type visit(BooleanValue e){
    return new PrimitiveType(PrimitiveType.PrimitiveKind.BOOLEAN);
  }
  
  public Type visit(IntegerValue e){
    return new PrimitiveType(PrimitiveType.PrimitiveKind.INTEGER);
  }
  
  public Type visit(StringValue e){
    return new PrimitiveType(PrimitiveType.PrimitiveKind.STRING);
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
