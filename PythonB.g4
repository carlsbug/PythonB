/**
 * Define a grammar called PythonB
 * It is based on Python using curly braces, not indentation
 */
grammar PythonB;


stmt 	: expr NEWLINE
		| print_stmt
		| IDENTIFIER '=' expr NEWLINE
		| NEWLINE
		| declarations
		|
		;
		
stmt_list  : stmt ( ';' stmt )* ';';		
print_stmt : PRINT '(' '"' STRING '"' ')'
		   | PRINT'(' '\'' STRING '\'' ')'
		   ;
		   
declarations : IDENTIFIER '=' VALUE ';';
VALUE : INTEGER
	  ;

expr : expr mul_div_op expr 
	 | expr add_sub_op expr
	 | expr rel_op expr
	 | number
	 | IDENTIFIER
	 | '(' expr ')'
	 ;
		
		
variable : IDENTIFIER ;
number : sign? INTEGER ;
sign   : '+' | '-' ;

mul_div_op : MUL_OP | DIV_OP ;
add_sub_op : ADD_OP | SUB_OP ;
rel_op     : EQ_OP | NE_OP | LT_OP | LE_OP | GT_OP | GE_OP ;


PRINT   : 'print';
STRING : [a-zA-Z0-9]+; // + is zero or more, using + for empty string
IDENTIFIER : [a-zA-Z][a-zA-Z0-9]+ ; // * is one or more
INTEGER    : [0-9]+ ;


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



//r  : 'hello' ID ;         // match keyword hello followed by an identifier

//ID : [a-z]+ ;             // match lower-case identifiers
//
//WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines

