package org.manifold.compiler.front;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;

public class TestNamespaceIdentifier {

  private NamespaceIdentifier getInstance() {
    return new NamespaceIdentifier("whdl:is:cool");
  }

  @Test
  public void testStringConstructor() {
    NamespaceIdentifier identifier = new NamespaceIdentifier("whdl:is:cool");

    assertEquals(identifier.getName().size(), 3);
    assertEquals(identifier.getName().get(0), "whdl");
    assertEquals(identifier.getName().get(1), "is");
    assertEquals(identifier.getName().get(2), "cool");
  }

  @Test
  public void testListConstructor() {
    ArrayList<String> name = new ArrayList<>(3);
    name.add("whdl");
    name.add("is");
    name.add("cool");
    NamespaceIdentifier identifier = new NamespaceIdentifier(name);

    assertEquals(identifier.getName().size(), 3);
    assertEquals(identifier.getName().get(0), "whdl");
    assertEquals(identifier.getName().get(1), "is");
    assertEquals(identifier.getName().get(2), "cool");
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetNameImmutable() {
    getInstance().getName().add("NOT!");
  }

  @Test
  public void testToString(){
    String separator = NamespaceIdentifier.getSeparator();
    String expected = "whdl" + separator + "is" + separator + "cool";
    String actual = getInstance().toString();
    assertEquals(expected, actual);
  }

  @Test
  public void isEmpty_true() {
    NamespaceIdentifier name = new NamespaceIdentifier(new ArrayList<>());
    assertTrue(name.isEmpty());
  }

  @Test
  public void isEmpty_false() {
    assertFalse(getInstance().isEmpty());
  }

  @Test
  public void equals_true() {
    assertTrue(getInstance().equals(getInstance()));
  }

  @Test
  public void equals_itself_true() {
    NamespaceIdentifier id = getInstance();
    assertTrue(id.equals(id));
  }

  @Test
  public void equals_type_false() {
    assertFalse(getInstance().equals("foo"));
  }
}
