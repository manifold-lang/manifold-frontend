package org.whdl.frontend;

import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.UnsupportedOperationException;
import java.util.ArrayList;

public class TestNamespaceIdentifier {

  private NamespaceIdentifier getInstance() {
    ArrayList<String> name = new ArrayList<String>(3);
    name.add("whdl");
    name.add("is");
    name.add("cool");

    return new NamespaceIdentifier(name);
  }

  @Test
  public void testGetName() {
    NamespaceIdentifier identifier = getInstance();

    assertEquals(identifier.getName().size(), 3);
    assertEquals(identifier.getName().get(0), "whdl");
    assertEquals(identifier.getName().get(1), "is");
    assertEquals(identifier.getName().get(2), "cool");
  }

  @Test(expected=UnsupportedOperationException.class)
  public void testGetNameImmutable() {
    getInstance().getName().add("NOT!");
  }
}
