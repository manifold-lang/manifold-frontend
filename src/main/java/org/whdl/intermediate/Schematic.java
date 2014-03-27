package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;

import org.whdl.intermediate.definitions.ConnectionDefinition;
import org.whdl.intermediate.definitions.ConstraintDefinition;
import org.whdl.intermediate.definitions.EndpointDefinition;
import org.whdl.intermediate.definitions.NodeDefinition;
import org.whdl.intermediate.definitions.TypeDefinition;
import org.whdl.intermediate.exceptions.MultipleDefinitionException;

/**
 * A Schematic contains all the information needed by the intermediate representation.
 * This includes type definitions, node/connection definitions, node/connection instantiations,
 * and constraint definitions/instantiations.
 */
public class Schematic {
  private String name;
  
  // Maps containing object definitions for this schematic; they are all indexed by the (string) type-name of the object.
  private Map<String, TypeDefinition> typeDefinitions;
  private Map<String, EndpointDefinition> endpointDefinitions;
  private Map<String, NodeDefinition> nodeDefinitions;
  private Map<String, ConnectionDefinition> connectionDefinitions;
  private Map<String, ConstraintDefinition> constraintDefinitions;
  
  // Maps containing instantiated objects for this schematic; they are all indexed by the (string) instance-name of the object.
  private Map<String, Node> nodes;
  private Map<String, Connection> connections;
  private Map<String, Constraint> constraints;
  
  public Schematic(String name){
    this.name = name;
    
    this.typeDefinitions = new HashMap<String, TypeDefinition>();
    this.nodeDefinitions = new HashMap<String, NodeDefinition>();
    this.connectionDefinitions = new HashMap<String, ConnectionDefinition>();
    this.constraintDefinitions = new HashMap<String, ConstraintDefinition>();

    this.nodes = new HashMap<String, Node>();
    this.connections = new HashMap<String, Connection>();
    this.constraints = new HashMap<String, Constraint>();
  }
  
  public void addTypeDefinition(TypeDefinition td) throws MultipleDefinitionException{
    String key = td.getTypename();
    if(typeDefinitions.containsKey(key)) throw new MultipleDefinitionException(key);
    typeDefinitions.put(key, td);
  }
  
  public void addEndpointDefinition(EndpointDefinition ed) throws MultipleDefinitionException{
    String key = ed.getTypename();
    if(endpointDefinitions.containsKey(key)) throw new MultipleDefinitionException(key);
    endpointDefinitions.put(key, ed);
  }
  
  public void addNodeDefinition(NodeDefinition nd) throws MultipleDefinitionException{
    String key = nd.getTypename();
    if(nodeDefinitions.containsKey(key)) throw new MultipleDefinitionException(key);
    nodeDefinitions.put(key, nd);
  }
  
  public void addConnectionDefinition(ConnectionDefinition cd) throws MultipleDefinitionException{
    String key = cd.getTypename();
    if(connectionDefinitions.containsKey(key)) throw new MultipleDefinitionException(key);
    connectionDefinitions.put(key, cd);
  }
  
  public void addConstraintDefinition(ConstraintDefinition cd) throws MultipleDefinitionException{
    String key = cd.getTypename();
    if(constraintDefinitions.containsKey(key)) throw new MultipleDefinitionException(key);
    constraintDefinitions.put(key, cd);
  }
  
  // FIXME do we add nodes as a function of their node definition right away, or just record that the node "will" exist with such-and-such definition and elaborate it later?
  
}
