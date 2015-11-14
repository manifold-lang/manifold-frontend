package org.manifold.compiler.front;

import org.junit.Test;
import org.junit.runners.Parameterized;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

/**
 * Tests that "*.manifold" files in "tests/acceptance" output an expected schematic.
 *
 * To add a test, add a ".manifold" file to "tests/acceptance".
 * The first time a test is run, a ".manifold.schematic" file will be generated.
 *
 * On future invocations, if the test runs and the output does not match that of the ".manifold.schematic",
 * an error will be thrown. To clear it, the ".manifold.schematic" file can be deleted.
 */
public class TestAcceptance extends AbstractTestFiles {

  @Parameterized.Parameters(name = "{0}")
  public static final Collection<File[]> filesToTest() {
    return getFilesToTest("tests/acceptance");
  }

  @Test
  public void acceptanceTest() throws Exception {
    // If it doesn't exist, the test will automatically create it.
    Path expectedSchematicPath = Paths.get(testFile.getAbsolutePath() + ".schematic");

    // If actual != expected, the actual will be stored here.
    // If actual == expected, the actual will be deleted.
    Path actualSchematicPath = Paths.get(testFile.getAbsolutePath() + ".schematic.actual");

    String expectedSchematic = null;
    if (Files.exists(expectedSchematicPath)) {
      byte[] encoded = Files.readAllBytes(expectedSchematicPath);
      // TODO(jnetterf): Support exciting encodings?
      expectedSchematic = new String(encoded, Charset.defaultCharset());
    }

    // The actual test:
    String actualSchematic = getSchematicJSON(testFile.getAbsolutePath());

    if (expectedSchematic != null) {
      log.debug("Checking schematic '" + expectedSchematicPath + "'");
      try {
        JSONAssert.assertEquals(
                actualSchematic,
                expectedSchematic,
                false /* don't be strict -- allow reordering */);
      } catch (AssertionError err) {
        FileWriter fileWriter = new FileWriter(actualSchematicPath.toFile());
        try {
          fileWriter.write(actualSchematic);
        } finally {
          fileWriter.close();
        }

        String explanation = new StringBuilder()
            .append("ERROR: This schematic does not match the expected schematic currently on disk.\n")
            .append("The new schematic has been saved as '")
            .append(actualSchematicPath.getFileName())
            .append("'.\n")
            .append("You may find a JSON diff tool (`npm install -g json-diff`) useful.\n")
            .append("If the changes are valid, regenerate it by deleting '")
            .append(expectedSchematicPath.getFileName())
            .append("' and running tests again.\n")
            .append(err)
            .toString();

        log.error(explanation);
        throw new AssertionError(explanation);
      }

      if (Files.exists(actualSchematicPath)) {
        Files.delete(actualSchematicPath);
      }
    } else {
      log.warn("Generating new expected schematic '" + expectedSchematicPath + "'");
    }

    FileWriter fileWriter = new FileWriter(expectedSchematicPath.toFile());
    try {
      fileWriter.write(actualSchematic);
    } finally {
      fileWriter.close();
    }
  }
}
