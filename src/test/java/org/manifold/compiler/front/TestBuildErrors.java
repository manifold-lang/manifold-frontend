package org.manifold.compiler.front;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runners.Parameterized;

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
public class TestBuildErrors extends AbstractTestFiles {
  @Parameterized.Parameters
  public static final Collection<File[]> filesToTest() {
    return getFilesToTest("tests/buildErrors");
  }

  @Test
  public void buildFailureTest() throws Exception {
    // If it doesn't exist, the test will automatically create it.
    Path expectedErrorPath = Paths.get(testFile.getAbsolutePath() + ".error");

    // If actual != expected, the actual will be stored here.
    // If actual == expected, the actual will be deleted.
    Path actualErrorPath = Paths.get(testFile.getAbsolutePath() + ".error.actual");

    String expectedError = null;
    if (Files.exists(expectedErrorPath)) {
      byte[] encoded = Files.readAllBytes(expectedErrorPath);
      expectedError = new String(encoded, Charset.defaultCharset());
      expectedError = normalizeLineEndings(expectedError);
    }

    boolean isCaught = false;
    try {
      getSchematicJSON(testFile.getAbsolutePath());
    } catch (FrontendBuildException error) {
      isCaught = true;
      String actualError = error.getMessage();
      actualError = normalizeLineEndings(actualError);

      if (expectedError == null) {
        FileWriter fileWriter = new FileWriter(expectedErrorPath.toFile());
        log.warn("Generating error output " + expectedErrorPath.toAbsolutePath());
        try {
          fileWriter.write(actualError);
        } finally {
          fileWriter.close();
        }
      } else if (!actualError.equals(expectedError)) {
        FileWriter fileWriter = new FileWriter(actualErrorPath.toFile());
        log.warn("Generating error output " + actualErrorPath.toAbsolutePath());
        try {
          fileWriter.write(actualError);
        } finally {
          fileWriter.close();
        }
        Assert.fail(new StringBuilder().append("The error in '")
                .append(actualErrorPath.getFileName())
                .append("' does not match that in '")
                .append(expectedErrorPath.getFileName())
                .append("'.\nIf the actual error is expected, remove '")
                .append(expectedErrorPath.getFileName())
                .append("' and run tests again.\n")
                .toString());
      } else if (Files.exists(actualErrorPath)) {
        Files.delete(actualErrorPath);
      }
    }

    if (!isCaught) {
      Assert.fail("Expected a FrontendBuildException to occur when invoking frontend for " +
              expectedErrorPath.toAbsolutePath());
    }
  }

  private String normalizeLineEndings(String fileContents) {
    return fileContents.replaceAll("\\r\\n?", "\n");
  }
}

