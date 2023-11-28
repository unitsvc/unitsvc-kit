grammar FilterExpr;

// ($age >= 30 && $name == 'John') || ($color == 'blue' && ($type == 'A' || $type == 'B'))
// ($age >= -3.14e-10 && $name @ [false,true,"1.2",123,0.234,-1.002,"字符串"] ) || ($color == "blue" && ($type == "A" || $type == "B"))
// ($age >= -3.14e-10 && $name @ [false,true,"1.2",123,0.234,-1.002,"字符串"] ) || ($color == $$bule && ($type == 'A' || $type == "B" || $type== $$C ))
// 语法表达式
// 关系词法表达式
query : orExpression ;
orExpression : andExpression ('||' andExpression)* ;
andExpression : variable ('&&' variable)* ;

// 变量词法表达式
variable: '(' variable ')'                                                                              # variableParents
    | '(' query ')'                                                                                     # queryParents
    | '$' ID op=('==' | '!=' | '>=' | '<=' | '>' | '<') value                                           # valueCondition
    | '$' ID '@' array                                                                                  # arrayCondition
    | '$' ID op=('==' | '!=' | '>=' | '<=' | '>' | '<') '$$' ID                                         # valueVarCondition
    | '$' ID '@' '$$' ID                                                                                # arrayVarCondition
    | '$' ID op=('like' | 'LIKE') value                                                                 # valueLikeCondition
    | '$' ID op=('l_like' | 'L_LIKE' | 'left_like' | 'LEFT_LIKE') value                                 # valueLikeLeftCondition
    | '$' ID op=('r_like' | 'R_LIKE' | 'right_like' | 'RIGHT_LIKE')  value                              # valueLikeRightCondition
    | '$' ID op=('l_like' | 'LIKE') '$$' ID                                                             # valueVarLikeCondition
    | '$' ID op=('l_like' | 'L_LIKE' | 'left_like' | 'LEFT_LIKE') '$$' ID                               # valueVarLikeLeftCondition
    | '$' ID op=('r_like' | 'R_LIKE' | 'right_like' | 'RIGHT_LIKE') '$$' ID                             # valueVarLikeRightCondition
    // 异常数据处理
    | '$' ID op=('==' | '!=' | '>=' | '<=' | '>' | '<')                                                 # emptyValueCondition
    | '$' ID '@'                                                                                        # emptyArrayCondition
    | '$' ID op=('like' | 'LIKE')                                                                       # emptyValueLikeCondition
    | '$' ID op=('l_like' | 'L_LIKE' | 'left_like' | 'LEFT_LIKE')                                       # emptyValueLikeLeftCondition
    | '$' ID op=('r_like' | 'R_LIKE' | 'right_like' | 'RIGHT_LIKE')                                     # emptyValueLikeRightCondition
    | '$' ID op=('==' | '!=' | '>=' | '<=' | '>' | '<') '$$'                                            # emptyValueVarCondition
    | '$' ID '@' '$$'                                                                                   # emptyArrayVarCondition
    | '$' ID op=('like' | 'LIKE') '$$'                                                                  # emptyValueVarLikeCondition
    | '$' ID op=('l_like' | 'L_LIKE' | 'left_like' | 'LEFT_LIKE') '$$'                                  # emptyValueVarLikeLeftCondition
    | '$' ID op=('r_like' | 'R_LIKE' | 'right_like' | 'RIGHT_LIKE') '$$'                                # emptyValueVarLikeRightCondition
    ;

array : '[' value (',' value)* ']'
        | 'eval' '(' JavaScript ')'
        ;

value : STRING
        | BOOL
        | NUMBER
        | NULL
        | 'eval' '('  JavaScript ')'
        ;

// 词法表达式，布尔数值空值类型优先解析
BOOL   : 'true' | 'false' ;                                             // 匹配：true/false
STRING : '"' ~["]* '"' | '\'' ~[']* '\'' ;                              // 匹配：双引号之间的字符串
NUMBER : ('+'|'-')? (DIGIT)+ ('.' (DIGIT)*)? ([eE]('+'|'-')?DIGIT+)? ;  // 匹配：整数，浮点数，科学计数法/-3.14e-10,-3.14E-10
NULL   : 'null' ;                                                       // 匹配：null
JavaScript: '```' .*? '```';                                            // 匹配：js表达式

// ID键次解析，后解析字符串
ID : [a-zA-Z_][a-zA-Z0-9_]* ('.' [a-zA-Z_][a-zA-Z0-9_]+)* ;             // 匹配：字母数字下划线，name,title.cn
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