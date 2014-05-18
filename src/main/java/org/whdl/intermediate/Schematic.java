package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;

/**
 * A Schematic contains all the information needed by the intermediate representation.
 * This includes type definitions, node/connection definitions, node/connection instantiations,
 * and constraint definitions/instantiations.
 */
public class Schematic {
  private String name;
  
  // Maps containing object definitions for this schematic; they are all indexed by the (string) type-name of the object.
  private Map<String, UserDefinedType> userDefinedTypes;
  private Map<String, EndpointType> endpointTypes;
  private Map<String, NodeType> nodeTypes;
  private Map<String, ConnectionType> connectionTypes;
  private Map<String, ConstraintType> constraintTypes;
  
  // Maps containing instantiated objects for this schematic; they are all indexed by the (string) instance-name of the object.
  private Map<String, Node> nodes;
  private Map<String, Connection> connections;
  private Map<String, Constraint> constraints;
  
  public Schematic(String name){
    this.name = name;
    
    this.userDefinedTypes = new HashMap<String, UserDefinedType>();
    populateDefaultTypeDefinitions();

    this.endpointTypes = new HashMap<String, EndpointType>();
    this.nodeTypes = new HashMap<String, NodeType>();
    this.connectionTypes = new HashMap<String, ConnectionType>();
    this.constraintTypes = new HashMap<String, ConstraintType>();

    this.nodes = new HashMap<String, Node>();
    this.connections = new HashMap<String, Connection>();
    this.constraints = new HashMap<String, Constraint>();
  }
  
  /*
   * Add "library standard" type definitions for basic types
   * such as integer, string, and boolean.
   * Every class in .intermediate.types should be represented in here.
   */
  private void populateDefaultTypeDefinitions(){
    UserDefinedType boolType = new UserDefinedType("Bool", new PrimitiveType(PrimitiveType.PrimitiveKind.BOOLEAN));
    UserDefinedType intType = new UserDefinedType("Int", new PrimitiveType(PrimitiveType.PrimitiveKind.INTEGER));    
    UserDefinedType stringType = new UserDefinedType("String", new PrimitiveType(PrimitiveType.PrimitiveKind.STRING));
    try{
      addTypeTypeDefinition(boolType);
      addTypeTypeDefinition(intType);
      addTypeTypeDefinition(stringType);
    }catch(MultipleDefinitionException mde){
      // this should not actually be possible unless there is something wrong with the compiler itself
      throw new UndefinedBehaviourError("could not create default type definitions (" + mde.getMessage() + ")");
    }
  }
  
  public void addTypeTypeDefinition(UserDefinedType td) throws MultipleDefinitionException{
    String key = td.getTypename();
    if(userDefinedTypes.containsKey(key)){
      throw new MultipleDefinitionException("type-definition", key);
    }
    userDefinedTypes.put(key, td);
  }
  
  public UserDefinedType getTypeTypeDefinition(String typename) throws UndeclaredIdentifierException {
    if(userDefinedTypes.containsKey(typename)){
      return userDefinedTypes.get(typename);
    }else{
      throw new UndeclaredIdentifierException(typename);
    }
  }
  
  public void addEndpointTypeDefinition(EndpointType ed) throws MultipleDefinitionException{
    String key = ed.getTypename();
    if(endpointTypes.containsKey(key)){
      throw new MultipleDefinitionException("endpoint-definition", key);
    }
    endpointTypes.put(key, ed);
  }
  
  public EndpointType getEndpointTypeDefinition(String typename) throws UndeclaredIdentifierException {
    if(endpointTypes.containsKey(typename)){
      return endpointTypes.get(typename);
    }else{
      throw new UndeclaredIdentifierException(typename);
    }
  }
  
  public void addNodeTypeDefinition(NodeType nd) throws MultipleDefinitionException{
    String key = nd.getTypename();
    if(nodeTypes.containsKey(key)){
      throw new MultipleDefinitionException("node-definition", key);
    }
    nodeTypes.put(key, nd);
  }
  
  public NodeType getNodeTypeDefinition(String typename) throws UndeclaredIdentifierException {
    if(nodeTypes.containsKey(typename)){
      return nodeTypes.get(typename);
    }else{
      throw new UndeclaredIdentifierException(typename);
    }
  }
  
  public void addConnectionTypeDefinition(ConnectionType cd) throws MultipleDefinitionException{
    String key = cd.getTypename();
    if(connectionTypes.containsKey(key)){
      throw new MultipleDefinitionException("connection-definition", key);
    }
    connectionTypes.put(key, cd);
  }
  
  public ConnectionType getConnectionTypeDefinition(String typename) throws UndeclaredIdentifierException {
    if(connectionTypes.containsKey(typename)){
      return connectionTypes.get(typename);
    }else{
      throw new UndeclaredIdentifierException(typename);
    }
  }
  
  public void addConstraintTypeDefinition(ConstraintType cd) throws MultipleDefinitionException{
    String key = cd.getTypename();
    if(constraintTypes.containsKey(key)){
      throw new MultipleDefinitionException("constraint-definition", key);
    }
    constraintTypes.put(key, cd);
  }
  
  public ConstraintType getConstraintTypeDefinition(String typename) throws UndeclaredIdentifierException {
    if(constraintTypes.containsKey(typename)){
      return constraintTypes.get(typename);
    }else{
      throw new UndeclaredIdentifierException(typename);
    }
  }
  
  // FIXME do we add nodes as a function of their node definition right away, or just record that the node "will" exist with such-and-such definition and elaborate it later?
  
}
