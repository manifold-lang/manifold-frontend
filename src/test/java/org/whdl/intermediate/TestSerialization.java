package org.whdl.intermediate;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

public class TestSerialization {

  private Schematic testSchematic;

  @Before
  public void setup() throws MultipleDefinitionException {

    testSchematic = new Schematic("test");
    Type t1 = IntegerType.getInstance();
    testSchematic.addUserDefinedTypeDefinition("type1", t1);

    ConstraintType c1 = new ConstraintType(new HashMap<String, Type>());
    testSchematic.addConstraintTypeDefinition("constraint1", c1);
    NodeType n1 = new NodeType(new HashMap<String, Type>(), new HashMap<String, PortType>());
    testSchematic.addNodeTypeDefinition("node1", n1);

    HashMap<String, Type> attributes = new HashMap<String, Type>();
    attributes.put("imakey", t1);
    PortType e1 = new PortType(attributes);
    testSchematic.addPortTypeDefinition("n1", e1);
  }

  @Test
  public void testAddConstraintDef() throws IOException {
    testSchematic.serialize(new BufferedWriter(new OutputStreamWriter(System.out)), true);
  }
}
