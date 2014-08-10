grammar Manifold;

// TODO type identifier vs value identifier

WHITESPACE: [ \t\r\n]+ -> skip;

////////////////////////////////////////////////////////
//                                                    //
//                        Values                      //
//                                                    //
////////////////////////////////////////////////////////

INTEGER_VALUE: [0-9]+;
BOOLEAN_VALUE: 'false' | 'true';

tupleValueEntry: (IDENTIFIER ':')? expression (':' expression)?;
tupleValue:
  '(' tupleValueEntry (',' tupleValueEntry)* ')' |
  '(' ')';

functionTypeValue: tupleValue '->' expression;
functionValue: functionTypeValue '{' (expression EXPRESSION_TERMINATOR)* '}';

////////////////////////////////////////////////////////
//                                                    //
//                     Identifiers                    //
//                                                    //
////////////////////////////////////////////////////////

IDENTIFIER: [a-zA-Z] [0-9a-zA-Z_]*;
namespacedIdentifier: (IDENTIFIER '::')* IDENTIFIER;

////////////////////////////////////////////////////////
//                                                    //
//                     Expressions                    //
//                                                    //
////////////////////////////////////////////////////////

expression:
  BOOLEAN_VALUE |
  INTEGER_VALUE |
  tupleValue |
  functionValue |
  expression expression |                         // function invocation
  expression '.' (IDENTIFIER | INTEGER_VALUE) |   // static attribute access
  namespacedIdentifier |                          // variable reference
  expression '=' expression;                      // assignment

EXPRESSION_TERMINATOR: ';';

////////////////////////////////////////////////////////
//                                                    //
//                      Schematic                     //
//                                                    //
////////////////////////////////////////////////////////

schematic: (expression EXPRESSION_TERMINATOR)*;