grammar Manifold;

// TODO type identifier vs value identifier

WHITESPACE: [ \t\r\n]+ -> skip;

COMMENT: '/*' .*? '*/' -> skip;

LINE_COMMENT: '//' ~[\r\n]* -> skip;

////////////////////////////////////////////////////////
//                                                    //
//                        Values                      //
//                                                    //
////////////////////////////////////////////////////////

INTEGER_VALUE: [0-9]+;
BOOLEAN_VALUE: 'false' | 'true';
TYPE_KEYWORD: 'Type';
STRING_VALUE: '"' ( '\"' | ~["] )*? '"';

VISIBILITY_PUBLIC: 'public';

tupleTypeValueEntry: (IDENTIFIER ':')? typevalue ('=' expression)?;
tupleTypeValue: '(' tupleTypeValueEntry (',' tupleTypeValueEntry)* ')';

tupleValueEntry: (IDENTIFIER '=')? expression;
tupleValue:
  '(' tupleValueEntry (',' tupleValueEntry)* ')' |
  '(' ')';

functionTypeValue: tupleTypeValue '->' tupleTypeValue;
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

type:
    namespacedIdentifier # Typename
  | tupleTypeValue # TupleType
  ;

typevalue:
    type # SimpleType
  | functionTypeValue # FunctionType
  ;

// TODO: implement type declarations and undefined variable declarations
//declaration:
//    type namespacedIdentifier # UndefinedDeclaration
//  | TYPE_KEYWORD namespacedIdentifier '=' type # TypeDeclaration
//  ;

reference:
    tupleValue # Tuple
  | reference '.' IDENTIFIER # StaticAttributeAccessExpression
  | reference '[' INTEGER_VALUE ']' # StaticAttributeAccessExpression
  | namespacedIdentifier # VariableReferenceExpression
  ;

// TODO implement arglist for multiple parameter passing to functions
// arglist:
//     rvalue arglist | ;

rvalue:
    BOOLEAN_VALUE # Boolean
  | INTEGER_VALUE # Integer
  | 'infer' # Infer
  | functionValue # Function
  | reference rvalue # FunctionInvocationExpression // TODO: function invocation needs to be 'reference arglist'
  | reference # ReferenceExpression
  | VISIBILITY_PUBLIC? lvalue '=' rvalue # AssignmentExpression
  | 'primitive' 'port' typevalue (':' tupleTypeValue)? # PrimitivePortDefinitionExpression
  | 'primitive' 'node' functionTypeValue # PrimitiveNodeDefinitionExpression
  | 'import' STRING_VALUE #ImportExpr
  ;

lvalue:
  // TODO: implement declarations as lvalues
  // declaration # AssignmentDeclaration
  reference # LValueExpression
  ;

// TODO: declarations as expressions
expression:
  rvalue
  // | declaration
  ;

EXPRESSION_TERMINATOR: ';';


////////////////////////////////////////////////////////
//                                                    //
//                      Schematic                     //
//                                                    //
////////////////////////////////////////////////////////

schematic: (expression EXPRESSION_TERMINATOR)*;
