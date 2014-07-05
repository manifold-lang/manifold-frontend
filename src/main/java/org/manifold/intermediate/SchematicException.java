package org.manifold.intermediate;

/**
 * Base class for errors caused by the data from the Schematic being invalid.
 *
 * This includes invalid/missing attributes or identifiers, or a Value of the
 * wrong Type.
 */
public abstract class SchematicException extends Exception {
  private static final long serialVersionUID = 5949898605273073499L;

}
