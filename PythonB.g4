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
				
print_stmt : PRINT '(' '"' variable '"' ')'
		   | PRINT'(' '\'' variable '\'' ')' 
		   ;
		   

expr : 
//		expr mul_div_op expr
//     | expr add_sub_op expr
//     | expr rel_op expr
     | number
     | IDENTIFIER
     | '(' expr ')'
     ;
     
number : sign? INTEGER ;
sign   : '+' | '-' ;

PRINT   : 'print';
variable : IDENTIFIER;
IDENTIFIER : [a-zA-Z][a-zA-Z0-9]* ;
		   
print_string : STRINGS;	
STRINGS : [a-z]+;	
INTEGER : [0-9]+;

		 // + is zero or more, using + for empty string

NEWLINE : '\r'? '\n' -> skip  ;
WS      : [ \t]+ -> skip ; 