package org.manifold.intermediate;

import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.manifold.intermediate.ConstraintType;
import org.manifold.intermediate.IntegerType;
import org.manifold.intermediate.MultipleDefinitionException;
import org.manifold.intermediate.NodeType;
import org.manifold.intermediate.PortType;
import org.manifold.intermediate.Schematic;
import org.manifold.intermediate.Type;

public class TestSerialization {

  private static final String TEST_SCHEMATIC_NAME = "dogematics";
  private static final String TEST_TYPE_NAME = "very type";
  private static final String TEST_CONSTRAINT_TYPE_NAME = "much constraint";
  private static final String TEST_NODE_TYPE_NAME = "such node";
  private static final String TEST_PORT_TYPE_NAME = "wow port";
  private static final String TEST_PORT_TYPE_ATTRIBUTE_NAME = "much attributes";

  private Schematic testSchematic;

  @Before
  public void setup() throws MultipleDefinitionException {

    testSchematic = new Schematic(TEST_SCHEMATIC_NAME);
    Type t1 = IntegerType.getInstance();
    testSchematic.addUserDefinedTypeDefinition(TEST_TYPE_NAME, t1);

    ConstraintType c1 = new ConstraintType(new HashMap<String, Type>());
    testSchematic.addConstraintTypeDefinition(TEST_CONSTRAINT_TYPE_NAME, c1);
    NodeType n1 = new NodeType(new HashMap<String, Type>(), new HashMap<String, PortType>());
    testSchematic.addNodeTypeDefinition(TEST_NODE_TYPE_NAME, n1);

    HashMap<String, Type> attributes = new HashMap<String, Type>();
    attributes.put(TEST_PORT_TYPE_ATTRIBUTE_NAME, t1);
    PortType e1 = new PortType(attributes);
    testSchematic.addPortTypeDefinition(TEST_PORT_TYPE_NAME, e1);
  }

  @Test
  public void testAddConstraintDef() throws IOException {
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
