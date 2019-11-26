/**
	 * Define a grammar called PythonB
 * It is based on Python using curly braces, not indentation
 */

grammar PythonB;

@header {
    import wci.intermediate.TypeSpec;
}

program   : mainBlock;
mainBlock : block;
block     : declarations funtion_stmt* NEWLINE main;
main: FUNCTIONDEF MAIN '(' parameter_list ')' NEWLINE '{' compoundStmt '}' ;

declarations : VAR declList;
declList     : decl (NEWLINE decl)*;
decl         : varList ':' typeId ;
varList      : varId ( ',' varId )* ;
varId        : NEWLINE* IDENTIFIER ;
typeId       : NEWLINE* IDENTIFIER ;

compoundStmt : stmt_list (NEWLINE stmt_list)*;
stmt_list    : stmt (NEWLINE stmt )*
			 ;

stmt : assignment_stmt
	 | print_stmt
	 | if_stmt
	 | while_loop
	 | funtion_stmt
	 |
	;

//assignment_stmt : NEWLINE* variable '=' expr NEWLINE*
assignment_stmt : variable '=' expr
				;
print_stmt :  //PRINT '(' character ')' #charExpPrint
		  // |
		   PRINT '(' string ')'
		   ;
// ? is 0 or 1 time
//if_stmt : IF NEWLINE* expr NEWLINE* '{' stmt_list '}' NEWLINE* (ELSE NEWLINE* '{' stmt_list '}' NEWLINE* )?;

if_stmt : IF expr NEWLINE '{' stmt_list '}' NEWLINE (ELSE NEWLINE '{' stmt_list '}')?;
//while_loop : WHILE NEWLINE* expr NEWLINE* '{' stmt_list '}';
while_loop : WHILE expr NEWLINE '{' stmt_list '}';
funtion_stmt : FUNCTIONDEF funt_name '(' parameter_list ')' NEWLINE '{'  stmt_list  '}' 
			 ; 

funt_name : IDENTIFIER 
		  ;

parameter_list : parameter (',' parameter)*;
parameter : IDENTIFIER
		  |
		  ;

expr locals [ TypeSpec type = null ]
	 : expr mulDivOp expr # mulDivExpr
     | expr addSubOp expr # addSubExpr
     | expr rel_op expr # relOpExpr
  	 | number               # unsignedNumberExpr
   	 | signedNumber         # signedNumberExpr
     | variable # variableExpr
     | '(' expr ')'  # parenExpr
     |  character # charExpr
     | string # stringExpr //want to make sure if I need this
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

mulDivOp : MUL_OP | DIV_OP ;
addSubOp : ADD_OP | SUB_OP ;
rel_op     : EQ_OP | NE_OP | LT_OP | LE_OP | GT_OP | GE_OP ;

IF : 'if';
ELSE : 'else';
WHILE : 'while';
FUNCTIONDEF : 'def';
PROGRAM: 'PROGRAM';
VAR: 'VAR';
MAIN: 'main';

INTEGER : [0-9]+;
FLOAT      : [0-9]+ '.' [0-9]+ ;
IDENTIFIER : [a-zA-Z][a-zA-Z0-9]*;

string :STRING
;


STRING : '\'' [a-zA-Z0-9~@#$%^&*()_+{}|:<>?,\\ ]*  '\''
		 | '"'[a-zA-Z0-9~@#$%^&*()_+{}|:<>?,\\ ]*  '"'
//		 |'"'.*'"'
//		 | '\''.*'\''
		 ;

character : CHAR
;

CHAR :'\''  ',' '\''
|'\'' '!''\'';

MUL_OP :   '*' ;
DIV_OP :   '/' ;
ADD_OP :   '+' ;
SUB_OP :   '-' ;

EQ_OP : '==' ;
NE_OP : '!=' ;
LT_OP : '<' ;
LE_OP : '<=' ;
GT_OP : '>' ;
GE_OP : '>=' ;

NEWLINE : [\r\n]+;
//NEWLINE : '\r'? '\n' -> skip  ;
WS      : [ \t]+ -> skip ;
