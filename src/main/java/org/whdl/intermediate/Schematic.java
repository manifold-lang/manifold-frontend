package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;

import org.whdl.intermediate.definitions.ConnectionDefinition;
import org.whdl.intermediate.definitions.ConstraintDefinition;
import org.whdl.intermediate.definitions.EndpointDefinition;
import org.whdl.intermediate.definitions.NodeDefinition;
import org.whdl.intermediate.definitions.TypeDefinition;
import org.whdl.intermediate.exceptions.MultipleDefinitionException;
import org.whdl.intermediate.types.BooleanType;
import org.whdl.intermediate.types.IntegerType;
import org.whdl.intermediate.types.StringType;

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
    populateDefaultTypeDefinitions();
    
    this.nodeDefinitions = new HashMap<String, NodeDefinition>();
    this.connectionDefinitions = new HashMap<String, ConnectionDefinition>();
    this.constraintDefinitions = new HashMap<String, ConstraintDefinition>();

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
    TypeDefinition boolType = new TypeDefinition("Bool", new BooleanType());
    TypeDefinition intType = new TypeDefinition("Int", new IntegerType());    
    TypeDefinition stringType = new TypeDefinition("String", new StringType());
    try{
      addTypeDefinition(boolType);
      addTypeDefinition(intType);
      addTypeDefinition(stringType);
    }catch(MultipleDefinitionException mde){
      // this should not actually be possible unless there is something wrong with the compiler itself
      throw new InternalError("could not create default type definitions (" + mde.getMessage() + ")");
    }
  }
  
  public void addTypeDefinition(TypeDefinition td) throws MultipleDefinitionException{
    String key = td.getTypename();
    if(typeDefinitions.containsKey(key)) throw new MultipleDefinitionException("type-definition", key);
    typeDefinitions.put(key, td);
  }
  
  public void addEndpointDefinition(EndpointDefinition ed) throws MultipleDefinitionException{
    String key = ed.getTypename();
    if(endpointDefinitions.containsKey(key)) throw new MultipleDefinitionException("endpoint-definition",key);
    endpointDefinitions.put(key, ed);
  }
  
  public void addNodeDefinition(NodeDefinition nd) throws MultipleDefinitionException{
    String key = nd.getTypename();
    if(nodeDefinitions.containsKey(key)) throw new MultipleDefinitionException("node-definition",key);
    nodeDefinitions.put(key, nd);
  }
  
  public void addConnectionDefinition(ConnectionDefinition cd) throws MultipleDefinitionException{
    String key = cd.getTypename();
    if(connectionDefinitions.containsKey(key)) throw new MultipleDefinitionException("connection-definition",key);
    connectionDefinitions.put(key, cd);
  }
  
  public void addConstraintDefinition(ConstraintDefinition cd) throws MultipleDefinitionException{
    String key = cd.getTypename();
    if(constraintDefinitions.containsKey(key)) throw new MultipleDefinitionException("constraint-definition",key);
    constraintDefinitions.put(key, cd);
  }
  
  // FIXME do we add nodes as a function of their node definition right away, or just record that the node "will" exist with such-and-such definition and elaborate it later?
  
}
