package org.manifold.compiler.front;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.misc.Nullable;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.manifold.parser.ManifoldLexer;
import org.manifold.parser.ManifoldParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.BitSet;
import java.util.List;

public class ExpressionGraphParser {

  private static Logger log = LogManager.getLogger("ExpressionGraphParser");

  public ExpressionGraph parseFile(File inputFile) throws IOException {
    ManifoldLexer lexer = new ManifoldLexer(new ANTLRInputStream(
        new FileInputStream(inputFile)));

    // Get a list of matched tokens
    CommonTokenStream tokens = new CommonTokenStream(lexer);

    // Pass the tokens to the parser
    ManifoldParser parser = new ManifoldParser(tokens);

    StringBuilder errors = new StringBuilder();
    parser.addErrorListener(new ANTLRErrorListener() {

      @Override
      public void syntaxError(@NotNull Recognizer<?, ?> recognizer, @Nullable Object offendingSymbol, int line,
                              int charPositionInLine, @NotNull String msg, @Nullable RecognitionException e) {
        errors.append("Error at line ").append(line).append(", char ")
            .append(charPositionInLine).append(": ").append(msg).append("\n");
      }

      @Override
      public void reportAmbiguity(@NotNull Parser recognizer, @NotNull DFA dfa, int startIndex, int stopIndex,
                                  boolean exact, @Nullable BitSet ambigAlts, @NotNull ATNConfigSet configs) {
        // Pass
      }

      @Override
      public void reportAttemptingFullContext(@NotNull Parser recognizer, @NotNull DFA dfa, int startIndex,
                                              int stopIndex, @Nullable BitSet conflictingAlts,
                                              @NotNull ATNConfigSet configs) {
        // Pass
      }

      @Override
      public void reportContextSensitivity(@NotNull Parser recognizer, @NotNull DFA dfa, int startIndex, int stopIndex,
                                           int prediction, @NotNull ATNConfigSet configs) {
        // Pass
      }
    });

    // Specify our entry point
    ManifoldParser.SchematicContext context = parser.schematic();

    if (errors.length() != 0) {
      throw new FrontendBuildException(errors.toString());
    }

    ExpressionContextVisitor graphBuilder = new ExpressionContextVisitor(inputFile);
    List<ManifoldParser.ExpressionContext> expressionContexts = context.expression();
    for (ManifoldParser.ExpressionContext expressionContext : expressionContexts) {
      graphBuilder.visit(expressionContext);
    }
    if (graphBuilder.getErrors().size() > 0) {
      throw new FrontendBuildException(
          String.join("\n", graphBuilder.getErrors()));
    }
    ExpressionGraph exprGraph = graphBuilder.getExpressionGraph();

    log.debug("writing out initial expression graph");
    File exprGraphDot = new File(inputFile.getName() + ".exprs.dot");
    exprGraph.writeDOTFile(exprGraphDot);
    return exprGraph;
  }
}
