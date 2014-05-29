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
  private Map<String, Type> userDefinedTypes;
  private Map<String, PortType> portTypes;
  private Map<String, NodeType> nodeTypes;
  private Map<String, ConnectionType> connectionTypes;
  private Map<String, ConstraintType> constraintTypes;
  
  // Maps containing instantiated objects for this schematic; they are all indexed by the (string) instance-name of the object.
  private Map<String, Node> nodes;
  private Map<String, Connection> connections;
  private Map<String, Constraint> constraints;
  
  public Schematic(String name){
    this.name = name;
    
    this.userDefinedTypes = new HashMap<String, Type>();
    populateDefaultTypeDefinitions();

    this.portTypes = new HashMap<String, PortType>();
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
    Type boolType = BooleanType.getInstance();
    Type intType = IntegerType.getInstance();
    Type stringType = StringType.getInstance();
    try{
      addUserDefinedTypeDefinition("Bool", boolType);
      addUserDefinedTypeDefinition("Int", intType);
      addUserDefinedTypeDefinition("String", stringType);
    }catch(MultipleDefinitionException mde){
      // this should not actually be possible unless there is something wrong with the compiler itself
      throw new UndefinedBehaviourError("could not create default type definitions (" + mde.getMessage() + ")");
    }
  }
  
  public void addUserDefinedTypeDefinition(String typename, Type td) throws MultipleDefinitionException{
    if(userDefinedTypes.containsKey(typename)){
      throw new MultipleDefinitionException("type-definition", typename);
    }
    userDefinedTypes.put(typename, td);
  }
  
  public Type getUserDefinedTypeDefinition(String typename) throws UndeclaredIdentifierException {
    if(userDefinedTypes.containsKey(typename)){
      return userDefinedTypes.get(typename);
    }else{
      throw new UndeclaredIdentifierException(typename);
    }
  }
  
  public void addPortTypeDefinition(String typename, PortType portType) throws MultipleDefinitionException{
    if(portTypes.containsKey(typename)){
      throw new MultipleDefinitionException("endpoint-definition", typename);
    }
    portTypes.put(typename, portType);
  }
  
  public PortType getPortTypeDefinition(String typename) throws UndeclaredIdentifierException {
    if(portTypes.containsKey(typename)){
      return portTypes.get(typename);
    }else{
      throw new UndeclaredIdentifierException(typename);
    }
  }
  
  public void addNodeTypeDefinition(String typename, NodeType nd) throws MultipleDefinitionException{
    if(nodeTypes.containsKey(typename)){
      throw new MultipleDefinitionException("node-definition", typename);
    }
    nodeTypes.put(typename, nd);
  }
  
  public NodeType getNodeTypeDefinition(String typename) throws UndeclaredIdentifierException {
    if(nodeTypes.containsKey(typename)){
      return nodeTypes.get(typename);
    }else{
      throw new UndeclaredIdentifierException(typename);
    }
  }
  
  public void addConnectionTypeDefinition(String typename, ConnectionType cd) throws MultipleDefinitionException{
    if(connectionTypes.containsKey(typename)){
      throw new MultipleDefinitionException("connection-definition", typename);
    }
    connectionTypes.put(typename, cd);
  }
  
  public ConnectionType getConnectionTypeDefinition(String typename) throws UndeclaredIdentifierException {
    if(connectionTypes.containsKey(typename)){
      return connectionTypes.get(typename);
    }else{
      throw new UndeclaredIdentifierException(typename);
    }
  }
  
  public void addConstraintTypeDefinition(String typename, ConstraintType cd) throws MultipleDefinitionException{
    if(constraintTypes.containsKey(typename)){
      throw new MultipleDefinitionException("constraint-definition", typename);
    }
    constraintTypes.put(typename, cd);
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
