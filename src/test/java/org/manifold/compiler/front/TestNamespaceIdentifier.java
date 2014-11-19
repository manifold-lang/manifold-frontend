package org.manifold.compiler.front;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TestNamespaceIdentifier {

  private NamespaceIdentifier getInstance() {
    return new NamespaceIdentifier("manifold::is::cool");
  }

  @Test
  public void testStringConstructor() {
    NamespaceIdentifier identifier =
        new NamespaceIdentifier("manifold::is::cool");

    assertEquals(identifier.getName().size(), 3);
    assertEquals(identifier.getName().get(0), "manifold");
    assertEquals(identifier.getName().get(1), "is");
    assertEquals(identifier.getName().get(2), "cool");
  }

  @Test
  public void testListConstructor() {
    ArrayList<String> name = new ArrayList<>(3);
    name.add("manifold");
    name.add("is");
    name.add("cool");
    NamespaceIdentifier identifier = new NamespaceIdentifier(name);

    assertEquals(identifier.getName().size(), 3);
    assertEquals(identifier.getName().get(0), "manifold");
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
    String expected = "manifold" + separator + "is" + separator + "cool";
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

  @Test
  public void anonymous_namespace_equals_itself() {
    NamespaceIdentifier id = new NamespaceIdentifier("");
    List<String> empty = new ArrayList<String>();
    NamespaceIdentifier id2 = new NamespaceIdentifier(empty);
    assertTrue(id.equals(id2));
  }

}
