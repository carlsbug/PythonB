/**
 * Define a grammar called PythonB
 * It is based on Python using curly braces, not indentation
 */
grammar PythonB;

stmt : assignment_stmt
	 | print_stmt
	;	
	
stmt_list       : stmt ( ';' stmt )* ';' ;
assignment_stmt : variable '=' expr
				;
				
print_stmt : PRINT '(' '"' print_string '"' ')'
		   | PRINT'(' '\'' print_string '\'' ')' 
		   ;
		   

expr : 
		expr mul_div_op expr
     | expr add_sub_op expr
     | expr rel_op expr
     | number
     | IDENTIFIER
     | '(' expr ')'
     ;
     
number : sign? INTEGER ;
sign   : '+' | '-' ;

PRINT   : 'print';
variable : IDENTIFIER;

mul_div_op : MUL_OP | DIV_OP ;
add_sub_op : ADD_OP | SUB_OP ;
rel_op     : EQ_OP | NE_OP | LT_OP | LE_OP | GT_OP | GE_OP ;



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

NEWLINE : '\r'? '\n' -> skip  ;
WS      : [ \t]+ -> skip ; 