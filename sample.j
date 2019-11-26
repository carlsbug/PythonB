.class public sample
.super java/lang/Object

.field private static _runTimer LRunTimer;
.field private static _standardIn LPascalTextIn;

; i,j:integer
.field private static i I
.field private static j I

; temp:char
.field private static temp C

; l:real
.field private static l F

.method public <init>()V

	aload_0
	invokenonvirtual    java/lang/Object/<init>()V
	return

.limit locals 1
.limit stack 1
.end method

.method public static main([Ljava/lang/String;)V

	new RunTimer
	dup
	invokenonvirtual RunTimer/<init>()V
	putstatic        null/_runTimer LRunTimer;
	new PascalTextIn
	dup
	invokenonvirtual PascalTextIn/<init>()V
	putstatic        null/_standardIn LPascalTextIn;

; i=32
	ldc	32
	putstatic	null/i I

; j=0
	ldc	0
	putstatic	null/j I

; l=32.22
	ldc	32.22
	putstatic	null/l F

; print("WelcometoPythonB\n")
	getstatic	java/lang/System/out Ljava/io/PrintStream;
	ldc	"Welcome to PythonB\n"
	invokevirtual	java/io/PrintStream.print(Ljava/lang/String;)V 

; ifi>j{j=1print('Yes,iisgreaterthanjandnowjis1\n')}else{j=0print('iisnotgreaterthanj\n')}
L000:
	getstatic	null/i I
	getstatic	null/j I
	if_icmpgt L001
; j=0
	ldc	0
	putstatic	null/j I

; print('iisnotgreaterthanj\n')
	getstatic	java/lang/System/out Ljava/io/PrintStream;
	ldc	"i is not greater than j\n"
	invokevirtual	java/io/PrintStream.print(Ljava/lang/String;)V 
	goto L002
L001:
; j=1
	ldc	1
	putstatic	null/j I

; print('Yes,iisgreaterthanjandnowjis1\n')
	getstatic	java/lang/System/out Ljava/io/PrintStream;
	ldc	"Yes, i is greater than j and now j is 1\n"
	invokevirtual	java/io/PrintStream.print(Ljava/lang/String;)V 

; while(j<5){print("hello\n")j=j+1}
L002:
	getstatic	null/j I
	ldc	5
	if_icmplt L003
	goto L004
L003:

; print("hello\n")
	getstatic	java/lang/System/out Ljava/io/PrintStream;
	ldc	"hello\n"
	invokevirtual	java/io/PrintStream.print(Ljava/lang/String;)V 

; j=j+1
	getstatic	null/j I
	ldc	1
	iadd
	putstatic	null/j I
	goto L002
L004:

	getstatic     null/_runTimer LRunTimer;
	invokevirtual RunTimer.printElapsedTime()V

	return

.limit locals 16
.limit stack 16
.end method
