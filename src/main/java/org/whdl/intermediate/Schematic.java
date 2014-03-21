package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;

import org.whdl.intermediate.definitions.ConnectionDefinition;
import org.whdl.intermediate.definitions.ConstraintDefinition;
import org.whdl.intermediate.definitions.EndpointDefinition;
import org.whdl.intermediate.definitions.NodeDefinition;
import org.whdl.intermediate.definitions.TypeDefinition;

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
  
}
