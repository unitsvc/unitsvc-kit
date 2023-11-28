grammar Expr;

// (age >= 30 && name == 'John') || (color == 'blue' && (type == 'A' || type == 'B'))
// (age >= -3.14e-10 && name @ [false,true,"1.2",123,0.234,-1.002,"字符串"] ) || (color == "blue" && (type == "A" || type == "B"))
// 语法表达式
expr: expr relation=('||' | '&&') expr                               # relation
    | '(' expr ')'                                                   # parens
    | ID op=('==' | '!=' | '>=' | '<=' | '>' | '<') value            # valueCondition
    | ID '@' array                                                   # arrayCondition
    ;

array : '[' value (',' value)* ']'
      ;

value : STRING
        | BOOL
        | NUMBER
        | NULL
        ;

// 词法表达式
STRING : '"' ~["]* '"' ;                                                // 匹配：双引号之间的字符串
BOOL   : 'true' | 'false' ;                                             // 匹配：true/false
NUMBER : ('+'|'-')? (DIGIT)+ ('.' (DIGIT)*)? ([eE]('+'|'-')?DIGIT+)? ;  // 匹配：整数，浮点数，科学计数法/-3.14e-10,-3.14E-10
NULL   : 'null' ;                                                       // 匹配：null

ID : [a-zA-Z]+ ('.' [a-zA-Z]+)* ;                                       // 匹配：name,title.cn
WS: [ \t\r\n]+ -> skip;

// 词法分析规则，语法片段，定义局部重复片段规则
fragment DIGIT : [0-9] ;                                                // 数字

AND: '&&';
OR: '||';

EQ: '==';
NE: '!=';
GTE: '>=';
LTE: '<=';
GT: '>';
LT: '<';
IN: '@';