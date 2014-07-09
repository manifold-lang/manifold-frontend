package org.manifold.compiler;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import org.manifold.compiler.back.Schematic;
import org.manifold.compiler.back.SchematicException;

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
  public void setup() throws SchematicException {

    testSchematic = new Schematic(TEST_SCHEMATIC_NAME);

    // port type
    PortTypeValue din = new PortTypeValue(new HashMap<>());
    PortTypeValue dout = new PortTypeValue(new HashMap<>());
    testSchematic.addPortType(DIGITAL_IN, din);
    testSchematic.addPortType(DIGITAL_OUT, dout);

    // node type
    HashMap<String, PortTypeValue> dinPortMap = new HashMap<>();
    dinPortMap.put(IN_PORT_NAME, din);

    HashMap<String, PortTypeValue> doutPortMap = new HashMap<>();
    doutPortMap.put(OUT_PORT_NAME, dout);

    NodeTypeValue dinNodeType = new NodeTypeValue(new HashMap<>(), dinPortMap);
    NodeTypeValue doutNodeType = new NodeTypeValue(
        new HashMap<>(),
        doutPortMap
    );

    testSchematic.addNodeType(IN_NODE_NAME, dinNodeType);
    testSchematic.addNodeType(OUT_NODE_NAME, doutNodeType);

    // node
    NodeValue inNode = new NodeValue(
        dinNodeType,
        new HashMap<>(),
        new HashMap<>()
    );
    
    NodeValue outNode = new NodeValue(
        doutNodeType,
        new HashMap<>(),
        new HashMap<>()
    );

    // connection
    ConnectionType conType = new ConnectionType(new HashMap<>());
    ConnectionValue con = new ConnectionValue(
        conType,
        inNode.getPort(IN_PORT_NAME),
        outNode.getPort(OUT_PORT_NAME),
        new HashMap<>()
    );
    
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
