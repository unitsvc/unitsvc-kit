grammar Expr;

// (age >= 30 && name == 'John') || (color == 'blue' && (type == 'A' || type == 'B'))
// 语法表达式
expr: expr relation=('||' | '&&') expr                      # relation
    | '(' expr ')'                                          # parens
    | ID op=('==' | '!=' | '>=' | '<=' | '>' | '<') value   # condition
    ;
value: number | str ;
str : SQSTR | DQSTR;
number
    : INT | decimal | negativeDecimal
    ;
decimal
    : INT '.' INT
    ;

negativeDecimal
    : '-' INT '.' INT
    ;

// 词法表达式
ID: [a-zA-Z]+;
INT     : [0-9]+ ;
SQSTR : '\'' (~['"] | DQSTR)* '\''; DQSTR : '"' (~['"] | SQSTR)* '"';
WS: [ \r\n] -> skip;

AND: '&&';
OR: '||';

EQ: '==';
NE: '!=';
GTE: '>=';
LTE: '<=';
GT: '>';
LT: '<';