package org.manifold.compiler.front;

import org.manifold.parser.ManifoldBaseVisitor;
import org.manifold.parser.ManifoldParser.ImportStatementContext;

import java.util.ArrayList;
import java.util.List;

class ImportVisitor extends ManifoldBaseVisitor<Void> {

  private List<String> imports;
  public List<String> getImports() {
    return this.imports;
  }

  public ImportVisitor() {
    this.imports = new ArrayList<>();
  }

  @Override
  public Void visitImportStatement(ImportStatementContext context) {
    String filePath = context.STRING_VALUE().getText();
    filePath = filePath.replaceAll("\\\"", "\"");
    this.imports.add(filePath.substring(1, filePath.length() - 1));
    return null;
  }
}
