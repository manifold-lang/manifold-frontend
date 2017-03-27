package org.manifold.compiler.front;

import com.google.gson.*;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.*;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.manifold.compiler.middle.Schematic;
import org.manifold.compiler.middle.serialization.SchematicSerializer;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import static org.apache.log4j.ConsoleAppender.*;

/**
 * Used to define tests that load and try to compile a set of manifold files.
 *
 * Subclass this by defining a static @Parameterized.Parameters function which returns the result of getFilesToTest.
 *
 * IMPORTANT: when subclassing, add a runtime dependency to build.gradle (search for "tests/acceptance").
 *
 * You can then implement one or more tests, which will be invoked for every file.
 *
 * The file under test can be accessed as 'testFile' variable.
 */
@RunWith(Parameterized.class)
public abstract class AbstractTestFiles {
  /**
   * Subclasses should have a static @Parameterized.Paramaters function which returns the result of this function.
   * @param searchPath e.g., "tests/acceptance"
   * @return An array of single-element arrays, each holding a File to test.
   */
  public static final Collection<File[]> getFilesToTest(String searchPath) {
    // Filter files that do not end in .manifold ...
    Pattern filterPattern = Pattern.compile(".*\\.manifold$");
    FileFilter filter = (File pathname) ->
          filterPattern.matcher(pathname.getName()).matches();

    // ... in "tests/acceptance"
    List<File> parameters = Arrays.asList(new File(searchPath).listFiles(filter));

    // Parameterized expects an array for each invocation, so map parameters to a
    // list of single-element arrays.
    List<File[]> formattedParameters = new ArrayList<File[]>();
    for (File parameter : parameters) {
      File[] singleParam = {parameter};
      formattedParameters.add(singleParam);
    }

    // Parameterized will create instance of TestFiles for each single-element
    // array in this collection.
    return formattedParameters;
  }

  /**
   * The first (and only) element in each element returned by filesToTest().
   * If another @Parameterized.Parameter were added below this one, it would refer to the
   * second element, and so on.
   */
  @Parameterized.Parameter
  public File testFile;

  protected static Logger log = LogManager.getLogger("TestAcceptance");

  /**
   * A wrapper around the frontend's `invokeFrontend` method.
   * Intended for use by getSchematicJSON.
   *
   * @param args Likely contains a file to test
   * @return The elaborated schematic
   * @throws Exception
   */
  private final Schematic invokeFrontend(String[] args) throws Exception {
    Options options = new Options();
    Main frontend = new Main();
    frontend.registerArguments(options);
    CommandLineParser parser = new PosixParser();
    CommandLine cli = parser.parse(options, args);
    return frontend.invokeFrontend(cli);
  }

  /**
   * Given a manifold source file, returns schematic JSON.
   * This a convenience function which returns a schematic JSON string.
   */
  protected final String getSchematicJSON(String pathName) throws Exception {
    String[] args = { pathName };
    Schematic actual = invokeFrontend(args);

    JsonObject schematicJson = SchematicSerializer.serialize(actual);

    Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    JsonParser jp = new JsonParser();
    JsonElement je = jp.parse(schematicJson.toString());
    return gson.toJson(je);
  }

  @BeforeClass
  public static void setupLogging() {
    PatternLayout layout = new PatternLayout(
            "%-5p [%t]: %m%n");
    LogManager.getRootLogger().setLevel(Level.WARN);
    LogManager.getRootLogger().removeAllAppenders();
    LogManager.getRootLogger().addAppender(
            new ConsoleAppender(layout, SYSTEM_ERR));
  }
}

