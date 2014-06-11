package org.manifold.intermediate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * A Schematic contains all the information needed by the intermediate
 * representation. This includes type definitions, node/connection definitions,
 * node/connection instantiations, and constraint definitions/instantiations.
 */
public class Schematic {
  private final String name;
  
  // Maps containing object definitions for this schematic; they are all
  // indexed by the (string) type-name of the object.
  private final Map<String, Type> userDefinedTypes;
  private final Map<String, PortType> portTypes;
  private final Map<String, NodeType> nodeTypes;
  private final Map<String, ConnectionType> connectionTypes;
  private final Map<String, ConstraintType> constraintTypes;
  
  // Maps containing instantiated objects for this schematic; they are all
  // indexed by the (string) instance-name of the object.
  private final Map<String, Node> nodes;
  private final Map<Node, String> reverseNodeMap;
  private final Map<String, Connection> connections;
  private final Map<String, Constraint> constraints;

  public Schematic(String name) {
    this.name = name;
    
    this.userDefinedTypes = new HashMap<>();
    populateDefaultType();

    this.portTypes = new HashMap<>();
    this.nodeTypes = new HashMap<>();
    this.connectionTypes = new HashMap<>();
    this.constraintTypes = new HashMap<>();

    this.nodes = new HashMap<>();
    this.reverseNodeMap = new HashMap<>();
    this.connections = new HashMap<>();
    this.constraints = new HashMap<>();
  }

  /*
   * Add "library standard" type definitions for basic types such as integer,
   * string, and boolean. Every class in .intermediate.types should be
   * represented in here.
   */
  private void populateDefaultType(){
    Type boolType = BooleanType.getInstance();
    Type intType = IntegerType.getInstance();
    Type stringType = StringType.getInstance();
    
    try {
      addUserDefinedType("Bool", boolType);
      addUserDefinedType("Int", intType);
      addUserDefinedType("String", stringType);
    } catch (MultipleDefinitionException mde) {
      // this should not actually be possible unless there is something wrong
      // with the compiler itself
      throw new UndefinedBehaviourError(
        "could not create default type definitions (" + mde.getMessage() + ")"
      );
    }
  }
  
  public void addUserDefinedType(String typename, Type td)
      throws MultipleDefinitionException{
    if (userDefinedTypes.containsKey(typename)){
      throw new MultipleDefinitionException("type-definition", typename);
    }
    userDefinedTypes.put(typename, td);
  }
  
  public Type getUserDefinedType(String typename)
      throws UndeclaredIdentifierException {
    if (userDefinedTypes.containsKey(typename)) {
      return userDefinedTypes.get(typename);
    } else {
      throw new UndeclaredIdentifierException(typename);
    }
  }
  
  public void addPortType(String typename, PortType portType)
      throws MultipleDefinitionException{
    if (portTypes.containsKey(typename)) {
      throw new MultipleDefinitionException("port-definition", typename);
    }
    portTypes.put(typename, portType);
  }
  
  public PortType getPortType(String typename)
      throws UndeclaredIdentifierException {
    if (portTypes.containsKey(typename)) {
      return portTypes.get(typename);
    } else {
      throw new UndeclaredIdentifierException(typename);
    }
  }
  
  public void addNodeType(String typename, NodeType nd)
      throws MultipleDefinitionException{
    if (nodeTypes.containsKey(typename)) {
      throw new MultipleDefinitionException("node-definition", typename);
    }
    nodeTypes.put(typename, nd);
  }
  
  public NodeType getNodeType(String typename)
      throws UndeclaredIdentifierException {
    
    if (nodeTypes.containsKey(typename)){
      return nodeTypes.get(typename);
    } else {
      throw new UndeclaredIdentifierException(typename);
    }
  }
  
  public void addConnectionType(String typename, ConnectionType cd)
      throws MultipleDefinitionException{
    if (connectionTypes.containsKey(typename)) {
      throw new MultipleDefinitionException("connection-definition", typename);
    }
    connectionTypes.put(typename, cd);
  }
  
  public ConnectionType getConnectionType(String typename)
      throws UndeclaredIdentifierException {
    if (connectionTypes.containsKey(typename)){
      return connectionTypes.get(typename);
    } else {
      throw new UndeclaredIdentifierException(typename);
    }
  }
  
  public void addConstraintType(String typename, ConstraintType cd)
      throws MultipleDefinitionException{
    if (constraintTypes.containsKey(typename)) {
      throw new MultipleDefinitionException("constraint-definition", typename);
    }
    constraintTypes.put(typename, cd);
  }
  
  public ConstraintType getConstraintType(String typename)
      throws UndeclaredIdentifierException {
    if (constraintTypes.containsKey(typename)) {
      return constraintTypes.get(typename);
    } else {
      throw new UndeclaredIdentifierException(typename);
    }
  }
  
  public void addNode(String instanceName, Node node)
      throws MultipleAssignmentException {
    if (nodes.containsKey(instanceName) || reverseNodeMap.containsKey(node)) {
      throw new MultipleAssignmentException("node", instanceName);
    }
    nodes.put(instanceName, node);
    reverseNodeMap.put(node, instanceName);
  }
  
  public Node getNode(String instanceName)
      throws UndeclaredIdentifierException {
    if (nodes.containsKey(instanceName)){
      return nodes.get(instanceName);
    } else {
      throw new UndeclaredIdentifierException(instanceName);
    }
  }
  
  public String getNodeName(Node instance) {
    if (reverseNodeMap.containsKey(instance)) {
      return reverseNodeMap.get(instance);
    }
    throw new NoSuchElementException();
  }
  
  public void addConnection(String instanceName, Connection conn)
      throws MultipleAssignmentException {
    if (connections.containsKey(instanceName)) {
      throw new MultipleAssignmentException("connection", instanceName);
    }
    connections.put(instanceName, conn);
  }
  
  public Connection getConnection(String instanceName)
       throws UndeclaredIdentifierException {
    if (connections.containsKey(instanceName)){
      return connections.get(instanceName);
    } else {
      throw new UndeclaredIdentifierException(instanceName);
    }
  }
  
  public void addConstraint(String instanceName, Constraint constraint)
      throws MultipleAssignmentException {
    if (constraints.containsKey(instanceName)){
      throw new MultipleAssignmentException("constraint", instanceName);
    }
    constraints.put(instanceName, constraint);
  }
  
  public Constraint getConstraint(String instanceName)
      throws UndeclaredIdentifierException {
    if (constraints.containsKey(instanceName)){
      return constraints.get(instanceName);
    } else {
      throw new UndeclaredIdentifierException(instanceName);
    }
  }

  // TODO do we add nodes as a function of their node definition right away, or
  // just record that the node "will" exist with such-and-such definition and
  // elaborate it later?

  public void serialize(BufferedWriter out, boolean pretty) throws IOException {
    Gson gson;
    if (pretty) {
      gson = new GsonBuilder().setPrettyPrinting().create();
    } else {
      gson = new Gson();
    }
    out.write(gson.toJson(this));
    out.flush();
  }

  public void serialize(BufferedWriter out) throws IOException {
    serialize(out, false);
  }

}
