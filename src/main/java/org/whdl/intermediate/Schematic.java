package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;

import org.whdl.intermediate.exceptions.MultipleDefinitionException;
import org.whdl.intermediate.exceptions.UndefinedBehaviourError;
import org.whdl.intermediate.types.PrimitiveType;

/**
 * A Schematic contains all the information needed by the intermediate representation.
 * This includes type definitions, node/connection definitions, node/connection instantiations,
 * and constraint definitions/instantiations.
 */
public class Schematic {
  private String name;
  
  // Maps containing object definitions for this schematic; they are all indexed by the (string) type-name of the object.
  private Map<String, TypeTypeDefinition> typeTypeDefinitions;
  private Map<String, EndpointTypeDefinition> endpointTypeDefinitions;
  private Map<String, NodeTypeDefinition> nodeTypeDefinitions;
  private Map<String, ConnectionTypeDefinition> connectionTypeDefinitions;
  private Map<String, ConstraintTypeDefinition> constraintTypeDefinitions;
  
  // Maps containing instantiated objects for this schematic; they are all indexed by the (string) instance-name of the object.
  private Map<String, Node> nodes;
  private Map<String, Connection> connections;
  private Map<String, Constraint> constraints;
  
  public Schematic(String name){
    this.name = name;
    
    this.typeTypeDefinitions = new HashMap<String, TypeTypeDefinition>();
    populateDefaultTypeDefinitions();

    this.endpointTypeDefinitions = new HashMap<String, EndpointTypeDefinition>();
    this.nodeTypeDefinitions = new HashMap<String, NodeTypeDefinition>();
    this.connectionTypeDefinitions = new HashMap<String, ConnectionTypeDefinition>();
    this.constraintTypeDefinitions = new HashMap<String, ConstraintTypeDefinition>();

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
    TypeTypeDefinition boolType = new TypeTypeDefinition("Bool", new PrimitiveType(PrimitiveType.PrimitiveKind.BOOLEAN));
    TypeTypeDefinition intType = new TypeTypeDefinition("Int", new PrimitiveType(PrimitiveType.PrimitiveKind.INTEGER));    
    TypeTypeDefinition stringType = new TypeTypeDefinition("String", new PrimitiveType(PrimitiveType.PrimitiveKind.STRING));
    try{
      addTypeTypeDefinition(boolType);
      addTypeTypeDefinition(intType);
      addTypeTypeDefinition(stringType);
    }catch(MultipleDefinitionException mde){
      // this should not actually be possible unless there is something wrong with the compiler itself
      throw new UndefinedBehaviourError("could not create default type definitions (" + mde.getMessage() + ")");
    }
  }
  
  public void addTypeTypeDefinition(TypeTypeDefinition td) throws MultipleDefinitionException{
    String key = td.getTypename();
    if(typeTypeDefinitions.containsKey(key)){
      throw new MultipleDefinitionException("type-definition", key);
    }
    typeTypeDefinitions.put(key, td);
  }
  
  public void addEndpointTypeDefinition(EndpointTypeDefinition ed) throws MultipleDefinitionException{
    String key = ed.getTypename();
    if(endpointTypeDefinitions.containsKey(key)){
      throw new MultipleDefinitionException("endpoint-definition", key);
    }
    endpointTypeDefinitions.put(key, ed);
  }
  
  public void addNodeTypeDefinition(NodeTypeDefinition nd) throws MultipleDefinitionException{
    String key = nd.getTypename();
    if(nodeTypeDefinitions.containsKey(key)){
      throw new MultipleDefinitionException("node-definition", key);
    }
    nodeTypeDefinitions.put(key, nd);
  }
  
  public void addConnectionTypeDefinition(ConnectionTypeDefinition cd) throws MultipleDefinitionException{
    String key = cd.getTypename();
    if(connectionTypeDefinitions.containsKey(key)){
      throw new MultipleDefinitionException("connection-definition", key);
    }
    connectionTypeDefinitions.put(key, cd);
  }
  
  public void addConstraintTypeDefinition(ConstraintTypeDefinition cd) throws MultipleDefinitionException{
    String key = cd.getTypename();
    if(constraintTypeDefinitions.containsKey(key)){
      throw new MultipleDefinitionException("constraint-definition", key);
    }
    constraintTypeDefinitions.put(key, cd);
  }
  
  // FIXME do we add nodes as a function of their node definition right away, or just record that the node "will" exist with such-and-such definition and elaborate it later?
  
}
