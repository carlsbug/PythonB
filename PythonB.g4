/**
 * Define a grammar called PythonB 
 * It is based on Python using curly braces, not indentation
 */
 
grammar PythonB;

@header {
    import wci.intermediate.TypeSpec;
}

//program : header mainBlock stmt_list
//		| header stmt_list
//		|
//		;
program : header mainBlock
		|
		;
header :;
mainBlock: block;
block : declarations compoundStmt;


declarations : decl_list;
decl_list    : decl (NEWLINE decl)* NEWLINE*;
decl         : var_id '=' values ;
//var_list     : var_id  (NEWLINE var_id)* NEWLINE*;
var_id       : IDENTIFIER;
//type_id      : string //string
//			 | number //int
//			 ;
values       :  number | string;


compoundStmt : stmt_list (NEWLINE stmt_list)* NEWLINE*;	 
stmt_list    :  NEWLINE* stmt (NEWLINE stmt )* NEWLINE* 
			 ;
			 
stmt : assignment_stmt 
	 | print_stmt
	 | if_stmt
	 | while_loop
	 | funtion_stmt
	 |
	;	
	 
assignment_stmt : NEWLINE* variable '=' expr NEWLINE*
				;	
print_stmt : PRINT '('string')'
		   ;
if_stmt : IF NEWLINE* expr NEWLINE* '{' stmt_list '}' NEWLINE* (ELSE NEWLINE* '{' stmt_list '}' NEWLINE* )?; // ? is 0 or 1 time
while_loop : WHILE NEWLINE* expr NEWLINE* '{' stmt_list '}';
funtion_stmt : FUNCTION funt_name NEWLINE* '(' parameter_list ')' NEWLINE*  '{'  stmt_list  '}';
 
funt_name : IDENTIFIER;

parameter_list : parameter (',' parameter)*;
parameter : IDENTIFIER
		  | 
		  ;

//expr : expr mul_div_op expr
//     | expr add_sub_op expr
//     | expr rel_op expr
//     | number
//     | IDENTIFIER
//     | '(' expr ')' 
//     | STRING
//     ;
expr locals [ TypeSpec type = null ] 
	 : expr mul_div_op expr # mulDivExpr
     | expr add_sub_op expr # addSubExpr
     | expr rel_op expr # relOpExpr
  	 | number               # unsignedNumberExpr
   	 | signedNumber         # signedNumberExpr
     | variable # variableExpr
     | '(' expr ')'  # parenExpr
     | string # stringExpr
     ;
     
//number : sign? INTEGER ;
number locals [ TypeSpec type = null ]
    : INTEGER    # integerConst
    | FLOAT      # floatConst
    ;
    
signedNumber locals [ TypeSpec type = null ] 
			: sign number 
			;
sign : ADD_OP | SUB_OP ;

PRINT   : 'print';
variable : IDENTIFIER;

mul_div_op : MUL_OP | DIV_OP ;
add_sub_op : ADD_OP | SUB_OP ;
rel_op     : EQ_OP | NE_OP | LT_OP | LE_OP | GT_OP | GE_OP ;

IF : 'if';
ELSE : 'else';
WHILE : 'while';
FUNCTION : 'def';

INTEGER : [0-9]+;
FLOAT      : [0-9]+ '.' [0-9]+ ;
IDENTIFIER : [a-zA-Z][a-zA-Z0-9]*;

string : STRING;
STRING : '\'' [a-zA-Z0-9~!@#$%^&*()_+{}|:<>?,\\ ]*  '\''
		 | '"'[a-zA-Z0-9~!@#$%^&*()_+{}|:<>?,\\ ]*  '"'
//		 |'"'.*'"' 
//		 | '\''.*'\''
		 ;	
		
MUL_OP :   '*' ;
DIV_OP :   '/' ;
ADD_OP :   '+' ;
SUB_OP :   '-' ;

EQ_OP : '=' ;
NE_OP : '<>' ;
LT_OP : '<' ;
LE_OP : '<=' ;
GT_OP : '>' ;
GE_OP : '>=' ;

NEWLINE : [\r\n]+;
//NEWLINE : '\r'? '\n' -> skip  ;
WS      : [ \t]+ -> skip ;