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

UNIT_TYPEVALUE: 'Unit';
INTEGER_TYPEVALUE: 'Int';
BOOLEAN_TYPEVALUE: 'Bool';
// STRING_TYPEVALUE: 'String';

tupleTypeValueEntry: (IDENTIFIER ':')? typevalue (':' expression)?;
tupleTypeValue: '(' tupleTypeValueEntry (',' tupleTypeValueEntry)* ')';

tupleValueEntry: (IDENTIFIER ':')? expression;
tupleValue:
  '(' tupleValueEntry (',' tupleValueEntry)* ')' |
  '(' ')';

functionTypeValue: tupleTypeValue '->' expression;
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

typevalue:
    UNIT_TYPEVALUE # UnitType
  | BOOLEAN_TYPEVALUE # BooleanType
  | INTEGER_TYPEVALUE # IntegerType
  | tupleTypeValue # TupleType
  | functionTypeValue # FunctionType
  ;

expression:
    BOOLEAN_VALUE # Boolean
  | INTEGER_VALUE # Integer
  | tupleValue # Tuple
  | functionValue # Function
  | expression expression # FunctionInvocation
  | expression '.' (IDENTIFIER | INTEGER_VALUE) # StaticAttributeAccess
  | namespacedIdentifier # VariableReference
  | expression '=' expression # Assignment
  ;

EXPRESSION_TERMINATOR: ';';

////////////////////////////////////////////////////////
//                                                    //
//                      Schematic                     //
//                                                    //
////////////////////////////////////////////////////////

schematic: (expression EXPRESSION_TERMINATOR)*;