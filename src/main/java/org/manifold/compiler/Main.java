package org.manifold.compiler;

import java.util.LinkedList;
import java.util.List;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.manifold.compiler.*;
import org.manifold.compiler.front.*;
import org.manifold.parser.*;

public class Main {

  public static void main(String[] args) throws Exception {

    Parser p = new Parser();
    
    p.parse(new ANTLRInputStream(System.in));

  }

}