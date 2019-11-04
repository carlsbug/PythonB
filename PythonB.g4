/**
 * Define a grammar called PythonB
 * It is based on Python using curly braces, not indentation
 */
grammar PythonB;

program :  stmt_list;
//stmt_list       :  stmt (NEWLINE stmt )* ;
stmt : assignment_stmt 
	 | print_stmt
	 | if_stmt
	 | while_loop
	 | funtion
	 | NEWLINE* '{' NEWLINE* stmt  NEWLINE* '}' NEWLINE*
	;	
	
stmt_list       :  stmt (NEWLINE stmt )* NEWLINE* ;
	 
assignment_stmt : variable '=' expr
				;	
print_stmt : PRINT '(' '"' print_string '"' ')' 
		   | PRINT'(' '\'' print_string '\'' ')' 
		   ;
if_stmt : IF expr  stmt (ELSE  stmt )?; // ? is 0 or 1 time
while_loop : WHILE expr stmt;
funtion : DEF funt_name '(' ')' stmt_list;
 
funt_name : IDENTIFIER;

expr : 
		expr mul_div_op expr
     | expr add_sub_op expr
     | expr rel_op expr
     | number
     | IDENTIFIER
     | '(' expr ')'
     | '\'' expr '\'' 
     | '"'  expr '"'  
     ;
     
number : sign? INTEGER ;
sign   : '+' | '-' ;

PRINT   : 'print';
variable : IDENTIFIER;

mul_div_op : MUL_OP | DIV_OP ;
add_sub_op : ADD_OP | SUB_OP ;
rel_op     : EQ_OP | NE_OP | LT_OP | LE_OP | GT_OP | GE_OP ;

IF : 'if';
ELSE : 'else';
WHILE : 'while';
DEF : 'def';

INTEGER : [0-9]+;
IDENTIFIER : [a-zA-Z][a-zA-Z0-9]*;

print_string : IDENTIFIER
			 | STRINGS
			 | 
			 ;
STRINGS : [a-zA-Z0-9]+;	


		 // + is zero or more, using + for empty string

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
