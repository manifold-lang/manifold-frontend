package org.manifold.intermediate;

import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.manifold.intermediate.Connection;
import org.manifold.intermediate.ConnectionType;
import org.manifold.intermediate.MultipleAssignmentException;
import org.manifold.intermediate.MultipleDefinitionException;
import org.manifold.intermediate.Node;
import org.manifold.intermediate.NodeType;
import org.manifold.intermediate.PortType;
import org.manifold.intermediate.Schematic;
import org.manifold.intermediate.Type;
import org.manifold.intermediate.UndeclaredIdentifierException;

public class TestSerialization {

  private static final String TEST_SCHEMATIC_NAME = "dogematics";
  private static final String TEST_TYPE_NAME = "very type";
  private static final String TEST_CONSTRAINT_TYPE_NAME = "much constraint";
  private static final String TEST_NODE_TYPE_NAME = "such node";
  private static final String TEST_PORT_TYPE_NAME = "wow port";
  private static final String TEST_PORT_TYPE_ATTRIBUTE_NAME = "much attributes";

  private static final String IN_PORT_NAME = "in_port_name";
  private static final String OUT_PORT_NAME = "out_port_name";

  private static final String DIGITAL_IN = "digital_in";
  private static final String DIGITAL_OUT = "digital_out";

  private static final String IN_NODE_NAME = "in_node_name";
  private static final String OUT_NODE_NAME = "out_node_name";
  
  private static final String CONNECTION_NAME = "wire";

  private Schematic testSchematic;

  @Before
  public void setup() throws MultipleDefinitionException, UndeclaredIdentifierException, MultipleAssignmentException {
    testSchematic = new Schematic(TEST_SCHEMATIC_NAME);

    // port type
    PortType din = new PortType(new HashMap<String, Type>());
    PortType dout = new PortType(new HashMap<String, Type>());
    testSchematic.addPortTypeDefinition(DIGITAL_IN, din);
    testSchematic.addPortTypeDefinition(DIGITAL_OUT, dout);

    // node type
    HashMap<String, PortType> dinPortMap = new HashMap<String, PortType>();
    dinPortMap.put(IN_PORT_NAME, din);

    HashMap<String, PortType> doutPortMap = new HashMap<String, PortType>();
    doutPortMap.put(OUT_PORT_NAME, dout);

    NodeType dinNodeType = new NodeType(new HashMap<String, Type>(), dinPortMap);
    NodeType doutNodeType = new NodeType(new HashMap<String, Type>(), doutPortMap);

    testSchematic.addNodeTypeDefinition(IN_NODE_NAME, dinNodeType);
    testSchematic.addNodeTypeDefinition(OUT_NODE_NAME, doutNodeType);

    // node
    Node inNode = new Node(dinNodeType);
    Node outNode = new Node(doutNodeType);

    // connection
    ConnectionType conType = new ConnectionType(new HashMap<String, Type>());
    Connection con = new Connection(conType, inNode.getPort(IN_PORT_NAME), outNode.getPort(OUT_PORT_NAME));
    
    testSchematic.addConnection(CONNECTION_NAME, con);
  }

  @Ignore
  @Test
  public void testSerialize() throws IOException {
    StringWriter outbuffer = new StringWriter();
    testSchematic.serialize(new BufferedWriter(outbuffer));
    String result = outbuffer.toString();

    assertTrue(result.contains(TEST_SCHEMATIC_NAME));
    assertTrue(result.contains(TEST_TYPE_NAME));
    assertTrue(result.contains(TEST_CONSTRAINT_TYPE_NAME));
    assertTrue(result.contains(TEST_NODE_TYPE_NAME));
    assertTrue(result.contains(TEST_PORT_TYPE_NAME));
    assertTrue(result.contains(TEST_PORT_TYPE_ATTRIBUTE_NAME));

    System.out.println(result);
  }
}
