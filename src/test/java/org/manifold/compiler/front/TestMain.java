package org.manifold.compiler.front;

import static org.junit.Assert.assertNotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.LogManager;
import org.apache.log4j.PatternLayout;
import org.junit.BeforeClass;
import org.junit.Test;
import org.manifold.compiler.middle.Schematic;

public class TestMain {

  @BeforeClass
  public static void setupLogging() {
    PatternLayout layout = new PatternLayout(
        "%-5p [%t]: %m%n");
    LogManager.getRootLogger().removeAllAppenders();
    LogManager.getRootLogger().addAppender(
        new ConsoleAppender(layout, ConsoleAppender.SYSTEM_ERR));
  }

  private File writeTempSchematic(String contents) throws IOException {
    File input = File.createTempFile("tmp", ".manifold");
    input.deleteOnExit();
    BufferedWriter writer = new BufferedWriter(new FileWriter(input));
    writer.write(contents);
    writer.flush();
    writer.close();
    return input;
  }

  private Schematic invokeFrontend(String[] args) throws Exception {
    Options options = new Options();
    Main frontend = new Main();
    frontend.registerArguments(options);
    CommandLineParser parser = new PosixParser();
    CommandLine cli = parser.parse(options, args);
    return frontend.invokeFrontend(cli);
  }

  @Test
  public void testSimpleElaboration() throws Exception {
    StringBuilder sb = new StringBuilder();
    sb
      .append("xIn = primitive port Bool; xOut = primitive port Bool;")
      .append("xNot = primitive node (x: xIn) -> (xbar: xOut);")
      .append("xInputPin = primitive node (Nil) -> (x: xOut);")
      .append("xOutputPin = primitive node (x: xIn) -> (Nil);")
      .append("a = xInputPin();")
      .append("b = xNot(x:a);")
      .append("xOutputPin(x:b);");
    File in = writeTempSchematic(sb.toString());
    String[] args = { in.getAbsolutePath() };
    Schematic actual = invokeFrontend(args);
    assertNotNull(actual);
    // TODO check for expected structure
  }

  @Test
  public void testNonPrimitiveElaboration() throws Exception {
    StringBuilder sb = new StringBuilder();
    sb
      .append("xIn = primitive port Bool; xOut = primitive port Bool;")
      .append("xNot = primitive node (x: xIn) -> (xbar: xOut);")
      .append("xInputPin = primitive node (Nil) -> (x: xOut);")
      .append("xOutputPin = primitive node (x: xIn) -> (Nil);")
      .append("invert = (p: Bool) -> (q: Bool) { q = xNot(x: p); };")
      .append("a = xInputPin();")
      .append("b = invert(p:a);")
      .append("xOutputPin(x:b);");
    File in = writeTempSchematic(sb.toString());
    String[] args = { in.getAbsolutePath() };
    Schematic actual = invokeFrontend(args);
    assertNotNull(actual);
    // TODO check for expected structure
  }

}
