package org.manifold.compiler.front;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.LogManager;
import org.apache.log4j.PatternLayout;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.manifold.compiler.BooleanTypeValue;
import org.manifold.compiler.BooleanValue;
import org.manifold.compiler.IntegerTypeValue;
import org.manifold.compiler.NilTypeValue;
import org.manifold.parser.ManifoldLexer;
import org.manifold.parser.ManifoldParser;
import org.manifold.parser.ManifoldParser.ExpressionContext;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TestExpressionContextVisitor {

  private ExpressionGraph exprGraph;
  private ExpressionContextVisitor visitor;

  @BeforeClass
  public static void setupLogging() {
    PatternLayout layout = new PatternLayout(
        "%-5p [%t]: %m%n");
    LogManager.getRootLogger().removeAllAppenders();
    LogManager.getRootLogger().addAppender(
        new ConsoleAppender(layout, ConsoleAppender.SYSTEM_ERR));
  }

  @AfterClass
  public static void afterClass() {
    LogManager.getRootLogger().removeAllAppenders();
  }

  @Before
  public void setup() {
    exprGraph = new ExpressionGraph();
    visitor = new ExpressionContextVisitor(exprGraph, new File("foo.manifold"));
  }

  private ExpressionContext parseExpression(String s) throws IOException {
    ManifoldLexer lexer = new ManifoldLexer(new ANTLRInputStream(
        new ByteArrayInputStream(s.getBytes())));

    // Get a list of matched tokens
    CommonTokenStream tokens = new CommonTokenStream(lexer);

    // Pass the tokens to the parser
    ManifoldParser parser = new ManifoldParser(tokens);

    // Specify our entry point
    ManifoldParser.SchematicContext context = parser.schematic();

    return context.expression().get(0);
  }

  @Test
  public void visitAssignment() throws IOException {
    ExpressionContext expression =  parseExpression("a = 2;");
    expression.accept(visitor);

    List<ExpressionEdge> edges = exprGraph.getEdges();
    assertEquals(edges.size(), 1);
    ExpressionEdge edge = edges.get(0);
    assertTrue(edge.getSource() instanceof ConstantValueVertex);

    assertTrue(edge.getTarget() instanceof VariableReferenceVertex);
    assertFalse(((VariableReferenceVertex) edge.getTarget()).getExported());
  }

  @Test
  public void visitAssignment_public() throws IOException {
    ExpressionContext expression =  parseExpression("public a = 2;");
    expression.accept(visitor);

    ExpressionEdge edge = exprGraph.getEdges().get(0);
    assertTrue(((VariableReferenceVertex) edge.getTarget()).getExported());
  }

  @Test
  public void visitInvocation() throws IOException {
    ExpressionContext expression =  parseExpression("b(2);");
    FunctionInvocationVertex v = (FunctionInvocationVertex) expression.accept(visitor);
    assertTrue(exprGraph.allVertices.contains(v));

    assertEquals(((VariableReferenceVertex) v.getFunctionEdge().getSource()).getId().getName(), "b");

    TupleValueVertex input = (TupleValueVertex) v.getInputEdge().getSource();

    assertEquals(input.getValueEdges().size(), 1);
    assertTrue(input.getValueEdges().get(0).getSource() instanceof ConstantValueVertex);
  }

  @Test
  public void visitFunctionValue_buildsFunctionStatements() throws IOException {
    ExpressionContext expression =  parseExpression(
        "(a: Bool) -> (b: Bool) {\n" +
        "  b = a;\n" +
        "};");
    FunctionValueVertex v = (FunctionValueVertex) expression.accept(visitor);
    assertTrue(exprGraph.allVertices.contains(v));

    VariableIdentifier vId = new VariableIdentifier(Arrays.asList("b"));
    assertTrue(v.getFunctionBody().containsVariable(vId));
    assertTrue(exprGraph.allVertices.contains(v));
  }

  @Test
  public void visitFunctionValue_buildsFunctionType() throws IOException {
    ExpressionContext expression =  parseExpression(
        "(a: Bool) -> (b: Bool) {\n" +
        "  b = a;\n" +
        "};");
    FunctionValueVertex v = (FunctionValueVertex) expression.accept(visitor);
    assertTrue(exprGraph.allVertices.contains(v));

    FunctionTypeValueVertex ft = (FunctionTypeValueVertex) v.getFunctionTypeEdge().getSource();

    TupleTypeValueVertex inputType = (TupleTypeValueVertex) ft.getInputTypeEdge().getSource();
    assertEquals(inputType.getTypeValueEdges().size(), 1);

    TupleTypeValueVertex outputType = (TupleTypeValueVertex) ft.getOutputTypeEdge().getSource();
    assertEquals(outputType.getTypeValueEdges().size(), 1);
  }

  @Test
  public void visitPrimitiveNodeDefinition() throws IOException {
    ExpressionContext expression =  parseExpression(
        "primitive node (x: Bool) -> (y: Bool);");
    PrimitiveNodeVertex v = (PrimitiveNodeVertex) expression.accept(visitor);
    assertTrue(exprGraph.allVertices.contains(v));

    FunctionTypeValueVertex ft = (FunctionTypeValueVertex) v.getSignatureEdge().getSource();

    TupleTypeValueVertex inputType = (TupleTypeValueVertex) ft.getInputTypeEdge().getSource();
    assertEquals(inputType.getTypeValueEdges().size(), 1);

    TupleTypeValueVertex outputType = (TupleTypeValueVertex) ft.getOutputTypeEdge().getSource();
    assertEquals(outputType.getTypeValueEdges().size(), 1);
  }

  @Test
  public void visitPrimitivePortDefinition_noAttributes() throws Exception {
    ExpressionContext expression =  parseExpression(
        "primitive port Bool;");
    PrimitivePortVertex v = (PrimitivePortVertex) expression.accept(visitor);
    assertTrue(exprGraph.allVertices.contains(v));

    v.elaborate();
    assertTrue(v.getSignalTypeEdge().getSource().getValue() instanceof BooleanTypeValue);
    assertTrue(v.getAttributesEdge().getSource().getValue() instanceof NilTypeValue);
  }

  @Test
  public void visitTupleTypeValue_positional() throws Exception {
    // Parses a function definition but the assertions are only with respect to the type
    ExpressionContext expression =  parseExpression(
        "(Bool, Int) -> (Bool) {};");
    ManifoldParser.TupleTypeValueContext context = ((ManifoldParser.FunctionContext) expression.rvalue())
        .functionValue().functionTypeValue().tupleTypeValue(0);
    TupleTypeValueVertex v = (TupleTypeValueVertex) context.accept(visitor);
    assertTrue(exprGraph.allVertices.contains(v));

    v.elaborate();
    assertEquals(v.getTypeValueEdges().size(), 2);
    assertTrue(v.getTypeValueEdges().get(0).getSource().getValue() instanceof BooleanTypeValue);
    assertTrue(v.getTypeValueEdges().get(1).getSource().getValue() instanceof IntegerTypeValue);
  }

  @Test
  public void visitTupleTypeValue_named() throws Exception {
    // Parses a function definition but the assertions are only with respect to the type
    ExpressionContext expression =  parseExpression(
        "(a: Bool, b: Int) -> (c: Bool) {};");
    ManifoldParser.TupleTypeValueContext context = ((ManifoldParser.FunctionContext) expression.rvalue())
        .functionValue().functionTypeValue().tupleTypeValue(0);
    TupleTypeValueVertex v = (TupleTypeValueVertex) context.accept(visitor);
    assertTrue(exprGraph.allVertices.contains(v));

    v.elaborate();
    assertEquals(v.getTypeValueEdges().size(), 2);
    assertTrue(v.getTypeValueEdges().get("a").getSource().getValue() instanceof BooleanTypeValue);
    assertTrue(v.getTypeValueEdges().get("b").getSource().getValue() instanceof IntegerTypeValue);
  }

  @Test
  public void visitTupleTypeValue_defaultValue() throws IOException {
    // Parses a function definition but the assertions are only with respect to the type
    ExpressionContext expression =  parseExpression(
        "(a: Bool = true, b: Int = 2) -> (c: Bool) {};");
    ManifoldParser.TupleTypeValueContext context = ((ManifoldParser.FunctionContext) expression.rvalue())
        .functionValue().functionTypeValue().tupleTypeValue(0);
    TupleTypeValueVertex v = (TupleTypeValueVertex) context.accept(visitor);
    assertTrue(exprGraph.allVertices.contains(v));

    assertEquals(v.getDefaultValueEdges().size(), 2);
    assertEquals(v.getDefaultValueEdges().get("a").getSource().getValue(), BooleanValue.getInstance(true));
    assertEquals(v.getDefaultValueEdges().get("b").getSource().getValue().toString(), "2");
  }

  @Test
  public void visitTupleValue_positional() throws IOException {
    ExpressionContext expression =  parseExpression(
        "x = (1, 2);");
    ManifoldParser.RvalueContext context = expression.rvalue();
    TupleValueVertex v = (TupleValueVertex) context.accept(visitor);
    assertTrue(exprGraph.allVertices.contains(v));

    assertEquals(v.getValueEdges().size(), 2);
    assertEquals(v.getValueEdges().get(0).getSource().getValue().toString(), "1");
    assertEquals(v.getValueEdges().get(1).getSource().getValue().toString(), "2");
  }

  @Test
  public void visitTupleValue_named() throws IOException {
    ExpressionContext expression =  parseExpression(
        "x = (a=1, b=2);");
    ManifoldParser.RvalueContext context = expression.rvalue();
    TupleValueVertex v = (TupleValueVertex) context.accept(visitor);
    assertTrue(exprGraph.allVertices.contains(v));

    assertEquals(v.getValueEdges().size(), 2);
    assertEquals(v.getValueEdges().get("a").getSource().getValue().toString(), "1");
    assertEquals(v.getValueEdges().get("b").getSource().getValue().toString(), "2");
  }

  @Test
  public void visitTupleValue_unpackPositional() throws IOException {
    ExpressionContext expression =  parseExpression(
        "(a, b) = (1, 2);");
    TupleValueVertex v = (TupleValueVertex) expression.accept(visitor);
    ExpressionVertex tupleVertex = exprGraph.getEdgesFromSource(v).get(0).getTarget();

    VariableReferenceVertex va = exprGraph.getVariableVertex(new VariableIdentifier(Arrays.asList("a")));
    List<ExpressionEdge> aEdges = exprGraph.getEdgesToTarget(va);
    assertEquals(aEdges.size(), 1);

    StaticNumberAttributeAccessVertex num = (StaticNumberAttributeAccessVertex) aEdges.get(0).getSource();
    assertEquals(num.getAttributeIDX(), 0);
    assertEquals(num.exprEdge.getSource(), tupleVertex);
  }

  @Test
  public void visitTupleValue_unpackNamed() throws IOException {
    ExpressionContext expression =  parseExpression(
        "(b=a) = (b=1, 2);");
    TupleValueVertex v = (TupleValueVertex) expression.accept(visitor);
    ExpressionVertex tupleVertex = exprGraph.getEdgesFromSource(v).get(0).getTarget();

    VariableReferenceVertex va = exprGraph.getVariableVertex(new VariableIdentifier(Arrays.asList("a")));
    List<ExpressionEdge> aEdges = exprGraph.getEdgesToTarget(va);
    assertEquals(aEdges.size(), 1);

    StaticStringAttributeAccessVertex num = (StaticStringAttributeAccessVertex) aEdges.get(0).getSource();
    assertEquals(num.getAttributeID(), "b");
    assertEquals(num.exprEdge.getSource(), tupleVertex);
  }

  @Test
  public void visitTupleValue_mustUnpackToVar() throws IOException {
    ExpressionContext expression =  parseExpression(
        "(1, b) = (1, 2);");
    try {
      expression.accept(visitor);
    } catch (FrontendBuildException err) {
      assertTrue(err.getMessage().contains("target must be a variable"));
    }
  }

  @Test
  public void visitTupleValue_mustUnpackPositionalFirst() throws IOException {
    ExpressionContext expression =  parseExpression(
        "(a=a, c) = (a=1, b=2);");
    try {
      expression.accept(visitor);
    } catch (FrontendBuildException err) {
      assertTrue(err.getMessage().contains("Index-based entries"));
    }
  }

  @Test
  public void visitVariable_private() throws IOException {
    ExpressionContext expression =  parseExpression(
        "a = 2;");
    expression.accept(visitor);
    VariableReferenceVertex v = exprGraph.getVariableVertex(new VariableIdentifier(Arrays.asList("a")));
    assertFalse(v.getExported());
  }

  @Test
  public void visitVariable_namespaced() throws IOException {
    ExpressionContext expression =  parseExpression(
        "a::b = 2;");
    expression.accept(visitor);
    // Will throw if variable is not found
    exprGraph.getVariableVertex(new VariableIdentifier(Arrays.asList("a", "b")));
  }

  @Test
  public void visitVariable_export() throws IOException {
    ExpressionContext expression =  parseExpression(
        "public a = 2;");
    expression.accept(visitor);
    VariableReferenceVertex v = exprGraph.getVariableVertex(new VariableIdentifier(Arrays.asList("a")));
    assertTrue(v.getExported());
  }

  @Test
  public void visitImport_library() throws IOException {
    ExpressionContext expression = parseExpression(
        "c = import \"circuits.manifold\";");
    ImportVertex v = (ImportVertex) expression.rvalue().accept(visitor);
    assertNotNull(v);
    assertTrue(exprGraph.allVertices.contains(v));
    assertEquals(v.getFile().getName().toString(), "circuits.manifold");
  }

  @Test
  public void visitImport_libraryNoExtension() throws IOException {
    ExpressionContext expression = parseExpression(
        "c = import \"circuits\";");
    ImportVertex v = (ImportVertex) expression.rvalue().accept(visitor);
    assertNotNull(v);
    assertTrue(exprGraph.allVertices.contains(v));
    assertEquals(v.getFile().getName().toString(), "circuits.manifold");
  }

  @Test
  public void visitImport_notFound() throws IOException {
    ExpressionContext expression = parseExpression(
        "c = import \"foo\";");
    ImportVertex v = (ImportVertex) expression.rvalue().accept(visitor);
    assertNull(v);
    assertEquals(visitor.getErrors().size(), 1);
  }

  @Test
  public void visitStaticAccess_number() throws IOException {
    ExpressionContext expression = parseExpression(
        "c = a[2];");
    StaticNumberAttributeAccessVertex v = (StaticNumberAttributeAccessVertex) expression.rvalue().accept(visitor);
    assertTrue(exprGraph.allVertices.contains(v));
    assertEquals(v.getAttributeIDX(), 2);
  }

  @Test
  public void visitStaticAccess_string() throws IOException {
    ExpressionContext expression = parseExpression(
        "c = a.b;");
    StaticStringAttributeAccessVertex v = (StaticStringAttributeAccessVertex) expression.rvalue().accept(visitor);
    assertTrue(exprGraph.allVertices.contains(v));
    assertEquals(v.getAttributeID(), "b");
  }
}

