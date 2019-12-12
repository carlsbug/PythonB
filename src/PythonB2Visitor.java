import java.io.PrintWriter;
import java.util.Hashtable;

import org.antlr.v4.runtime.tree.ParseTree;

import wci.intermediate.*;
import wci.intermediate.symtabimpl.*;

public class PythonB2Visitor extends PythonBBaseVisitor<Integer> {
	private String programName;
	private PrintWriter jFile;
	private static int index = 0;
	private boolean methodFlag = false;
	Hashtable<String, Integer> h;

	public PythonB2Visitor(PrintWriter jFile, String inputFile, Hashtable<String, Integer> h) {
		this.jFile = jFile;
		programName = inputFile;
		this.h = h;
	}

	@Override
	public Integer visitProgram(PythonBParser.ProgramContext ctx) {
		Integer value = visitChildren(ctx);
		jFile.close();
		return value;
	}

	@Override
	public Integer visitBlock(PythonBParser.BlockContext ctx) {
		
		return visitChildren(ctx);
	}
	
	@Override
	public Integer visitWithDeclar(PythonBParser.WithDeclarContext ctx) {
		
//		String funcName = ctx.funt_name().getText();
		methodFlag = true;
//		jFile.println(".method private static " + funcName+ "()V");
		
//		Integer value = visitChildren(ctx);
		
		return visitChildren(ctx);
	}

	@Override
	public Integer visitNoDeclar(PythonBParser.NoDeclarContext ctx) {
		
//		String funcName = ctx.funt_name().getText();
		methodFlag = true;
//		jFile.println(".method private static " + funcName+ "()V");
//		Integer value = visitChildren(ctx);
		return visitChildren(ctx);
	}

	@Override
	public Integer visitMain(PythonBParser.MainContext ctx) {
		// Emit the main program header.
		jFile.println();
		jFile.println(".method public static main([Ljava/lang/String;)V");
		jFile.println();
		jFile.println("\tnew RunTimer");
		jFile.println("\tdup");
		jFile.println("\tinvokenonvirtual RunTimer/<init>()V");
		jFile.println("\tputstatic        " + programName + "/_runTimer LRunTimer;");
		jFile.println("\tnew PascalTextIn");
		jFile.println("\tdup");
		jFile.println("\tinvokenonvirtual PascalTextIn/<init>()V");
		jFile.println("\tputstatic        " + programName + "/_standardIn LPascalTextIn;");
		Integer value = visitChildren(ctx);
		// Emit the main program epilogue.
		jFile.println();
		jFile.println("\tgetstatic     " + programName + "/_runTimer LRunTimer;");
		jFile.println("\tinvokevirtual RunTimer.printElapsedTime()V");
		jFile.println();
		jFile.println("\treturn");
		jFile.println();
		jFile.println(".limit locals 16");
		jFile.println(".limit stack 16");
		jFile.println(".end method");
		return value;
	}

	@Override
	public Integer visitStmt(PythonBParser.StmtContext ctx) {
		String temp = ctx.getText();
		if (temp.replaceAll("\\s", "") != "")
			jFile.println("\n; " + temp.replaceAll("\\s", ""));

		return visitChildren(ctx);
	}

	// local variable of function
	@Override
	public Integer visitAssignment_stmt(PythonBParser.Assignment_stmtContext ctx) {
//		Integer value = visitChildren(ctx);
		Integer value = visit(ctx.expr());
		String typeIndicator = (ctx.expr().type == Predefined.integerType) ? "I"
				: (ctx.expr().type == Predefined.realType) ? "F" : (ctx.expr().type == Predefined.charType) ? "C" : "?";
		// Emit a field put instruction.
		if(methodFlag)
		{
			String type = typeIndicator.toLowerCase();
			
			jFile.println(
			"\t" + type + "store_" + h.get(ctx.variable().IDENTIFIER().toString()));
		}
		else
			
		{
			jFile.println(
					"\tputstatic\t" + programName + "/" + ctx.variable().IDENTIFIER().toString() + " " + typeIndicator);
		}
		
		
		return value;
	}

	@Override
	public Integer visitIf_stmt(PythonBParser.If_stmtContext ctx) {
		jFile.println("L" + String.format("%03d", index++) + ":");
		Integer value = visit(ctx.expr());
		jFile.print(" L" + String.format("%03d", index));
		value = visit(ctx.stmt_list(1));
		int count = index + 1;
		jFile.println("\tgoto L" + String.format("%03d", count));
		jFile.print("L" + String.format("%03d", index++) + ":");
		value = visit(ctx.stmt_list(0));
		
		return value;
	}
//	@Override
//	public Integer visitPrint_stmt(PythonBParser.Print_stmtContext ctx) {
//		jFile.println("\tgetstatic\t" + "java/lang/System/out Ljava/io/PrintStream;");
//		Integer value = visit(ctx.string());
//		jFile.println("\tinvokevirtual\t" + "java/io/PrintStream.print(Ljava/lang/String;)V ");
//
//		return value;
//	}

	@Override
	public Integer visitPrint(PythonBParser.PrintContext ctx) {
		jFile.println("\tgetstatic\t" + "java/lang/System/out Ljava/io/PrintStream;");
		Integer value = visit(ctx.string());
		jFile.println("\tinvokevirtual\t" + "java/io/PrintStream.print(Ljava/lang/String;)V ");

		return value;
	}

	@Override
	public Integer visitSpecialMethod(PythonBParser.SpecialMethodContext ctx) {
		jFile.println("\tgetstatic\t" + "java/lang/System/out Ljava/io/PrintStream;");
		jFile.println("\tldc\t" + "\"나는 소현이다!\\n\"");
		jFile.println("\tinvokevirtual\t" + "java/io/PrintStream.print(Ljava/lang/String;)V ");
		jFile.println("\tgetstatic\t" + "java/lang/System/out Ljava/io/PrintStream;");
		jFile.println("\tldc\t" + "\"나는 소현이다!\\n\" ");
		jFile.println("\tinvokevirtual\t" + "java/io/PrintStream.print(Ljava/lang/String;)V ");
		jFile.println("\tgetstatic\t" + "java/lang/System/out Ljava/io/PrintStream;");
		jFile.println("\tldc\t" + "\"나는 소현이다!\\n\"");
		jFile.println("\tinvokevirtual\t" + "java/io/PrintStream.print(Ljava/lang/String;)V ");
		jFile.println("\tgetstatic\t" + "java/lang/System/out Ljava/io/PrintStream;");
		jFile.println("\tldc\t" + "\"나는 소현이다!\\n\"");
		jFile.println("\tinvokevirtual\t" + "java/io/PrintStream.print(Ljava/lang/String;)V ");
		jFile.println("\tgetstatic\t" + "java/lang/System/out Ljava/io/PrintStream;");
		jFile.println("\tldc\t" + "\"나는 소현이다!\\n\"");
		jFile.println("\tinvokevirtual\t" + "java/io/PrintStream.print(Ljava/lang/String;)V ");

		return visitChildren(ctx);
	}

	///////
	@Override
	public Integer visitWhile_loop(PythonBParser.While_loopContext ctx) {
		int temp = index;
		jFile.println("L" + String.format("%03d", index++) + ":");
		Integer value = visit(ctx.expr());
		jFile.println(" L" + String.format("%03d", index));
		jFile.println("\tgoto L" + String.format("%03d", temp + 2));
		jFile.println("L" + String.format("%03d", index++) + ":");
		value = visit(ctx.stmt_list());
		jFile.println("\tgoto L" + String.format("%03d", temp));
		jFile.println("L" + String.format("%03d", index) + ":");

		return value;
	}
//	@Override
//	public Integer visitFuntion_stmt(PythonBParser.Funtion_stmtContext ctx) {
//		return visitChildren(ctx);
//	}

	
	


	@Override
	public Integer visitStmt_list(PythonBParser.Stmt_listContext ctx) {
		Integer value = visitChildren(ctx);
		return value;
	}

	@Override
	public Integer visitRelOpExpr(PythonBParser.RelOpExprContext ctx) {
		Integer value = visitChildren(ctx);
		String op = ctx.rel_op().getText();
		String opcode;
		if (op.equals(">")) {
			opcode = "if_icmpgt";
		} else if (op.equals("<")) {
			opcode = "if_icmplt";
		} else if (op.equals(">=")) {
			opcode = "if_icmpge";
		} else if (op.equals("<=")) {
			opcode = "if_icmple";
		} else if (op.equals("==")) {
			opcode = "if_icmpeq";
		} else {
			opcode = "if_icmpne";
		}
		jFile.print("\t" + opcode);

		return value;
	}

	@Override
	public Integer visitAddSubExpr(PythonBParser.AddSubExprContext ctx) {
		Integer value = visitChildren(ctx);
		TypeSpec type1 = ctx.expr(0).type;
		TypeSpec type2 = ctx.expr(1).type;
		boolean integerMode = (type1 == Predefined.integerType) && (type2 == Predefined.integerType);
		boolean realMode = (type1 == Predefined.realType) && (type2 == Predefined.realType);

		String op = ctx.addSubOp().getText();
		String opcode;

		if (op.equals("+")) {
			opcode = integerMode ? "iadd" : realMode ? "fadd" : "????";
		} else {
			opcode = integerMode ? "isub" : realMode ? "fsub" : "????";
		}
		// Emit an add or subtract instruction.
		jFile.println("\t" + opcode);

		return value;
	}

	@Override
	public Integer visitMulDivExpr(PythonBParser.MulDivExprContext ctx) {
		Integer value = visitChildren(ctx);

		TypeSpec type1 = ctx.expr(0).type;
		TypeSpec type2 = ctx.expr(1).type;

		boolean integerMode = (type1 == Predefined.integerType) && (type2 == Predefined.integerType);
		boolean realMode = (type1 == Predefined.realType) && (type2 == Predefined.realType);

		String op = ctx.mulDivOp().getText();
		String opcode;

		if (op.equals("*")) {
			opcode = integerMode ? "imul" : realMode ? "fmul" : "f???";
		} else {
			opcode = integerMode ? "idiv" : realMode ? "fdiv" : "????";
		}

		// Emit a multiply or divide instruction.
		jFile.println("\t" + opcode);

		return value;
	}

	@Override
	public Integer visitVariableExpr(PythonBParser.VariableExprContext ctx) {
		String variableName = ctx.variable().IDENTIFIER().toString();
		TypeSpec type = ctx.type;
		String typeIndicator = (type == Predefined.integerType) ? "I"
				: (type == Predefined.realType) ? "F" : (type == Predefined.charType) ? "C" : "?";
		if(methodFlag)
		{
			jFile.println(
			"\taload"		);
		}
		else
			
		{
			jFile.println("\tgetstatic\t" + programName + "/" + variableName + " " + typeIndicator);
		}
		// Emit a field get instruction.
		

		return visitChildren(ctx);
	}

//	@Override
//	public Integer visitStringExpr(PythonBParser.StringExprContext ctx) {
//		Integer value = visitChildren(ctx);
//		return value;
//	}
	@Override
	public Integer visitString(PythonBParser.StringContext ctx) {
		String value = ctx.getText();
		if (value.charAt(0) == '\'') {
			value = "\"" + value.substring(1, value.length() - 1) + "\"";
			jFile.println("\tldc\t" + value);
		} else {
			jFile.println("\tldc\t" + ctx.getText());
		}
		return visitChildren(ctx);
	}

	@Override
	public Integer visitSignedNumber(PythonBParser.SignedNumberContext ctx) {
		Integer value = visitChildren(ctx);
		TypeSpec type = ctx.number().type;

		if (ctx.sign().getChild(0) == ctx.sign().SUB_OP()) {
			String opcode = (type == Predefined.integerType) ? "ineg" : (type == Predefined.realType) ? "fneg" : "?neg";

			// Emit a negate instruction.
			jFile.println("\t" + opcode);
		}

		return value;
	}

	@Override
	public Integer visitIntegerConst(PythonBParser.IntegerConstContext ctx) {
		// Emit a load constant instruction.
		jFile.println("\tldc\t" + ctx.getText());

		return visitChildren(ctx);
	}

	@Override
	public Integer visitFloatConst(PythonBParser.FloatConstContext ctx) {
		// Emit a load constant instruction.
		jFile.println("\tldc\t" + ctx.getText());

		return visitChildren(ctx);
	}
}
