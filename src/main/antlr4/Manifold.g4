grammar Manifold;

// TODO type identifier vs value identifier

WHITESPACE: [ \t\r\n]+ -> skip;

////////////////////////////////////////////////////////
//                                                    //
//                     Identifiers                    //
//                                                    //
////////////////////////////////////////////////////////

IDENTIFIER: [a-zA-Z] [0-9a-zA-Z_]*;
namespacedIdentifier: (IDENTIFIER '::')* IDENTIFIER;

////////////////////////////////////////////////////////
//                                                    //
//                        Values                      //
//                                                    //
////////////////////////////////////////////////////////

INTEGER_VALUE: [0-9]+;

tupleValueEntry: (IDENTIFIER ':')? expression (':' expression)?;
tupleValue:
  '(' tupleValueEntry (',' tupleValueEntry)* ')' |
  '(' ')';

functionTypeValue: tupleValue '->' expression;
functionValue: functionTypeValue '{' expressions '}';

////////////////////////////////////////////////////////
//                                                    //
//                     Expressions                    //
//                                                    //
////////////////////////////////////////////////////////

expression:
  INTEGER_VALUE |
  tupleValue |
  functionValue |
  expression expression |                         // function invocation
  expression '.' (IDENTIFIER | INTEGER_VALUE) |   // static attribute access
  namespacedIdentifier |                          // variable reference
  expression '=' expression;                      // assignment

EXPRESSION_TERMINATOR: ';';

expressions: (expression EXPRESSION_TERMINATOR)*;

////////////////////////////////////////////////////////
//                                                    //
//                      Schematic                     //
//                                                    //
////////////////////////////////////////////////////////

schematic: expressions;