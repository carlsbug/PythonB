import java.io.PrintWriter;

import org.antlr.v4.runtime.tree.ParseTree;

import wci.intermediate.*;
import wci.intermediate.symtabimpl.*;

public class PythonB2Visitor extends PythonBBaseVisitor<Integer> 
{
    String programName;
    private PrintWriter jFile;
    private static int index = 0;
    
    public PythonB2Visitor(PrintWriter jFile)
    {
        this.jFile = jFile;
    }
    
    @Override 
    public Integer visitProgram(PythonBParser.ProgramContext ctx) 
    { 
        Integer value = visitChildren(ctx); 
        jFile.close();
        return value;
    }
    
    @Override 
    public Integer visitHeader(PythonBParser.HeaderContext ctx) 
    { 
//        programName = ctx.IDENTIFIER().toString();
    	programName = "sample";
        return visitChildren(ctx); 
    }
    
    @Override 
    public Integer visitMainBlock(PythonBParser.MainBlockContext ctx) 
    { 
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
    public Integer visitStmt(PythonBParser.StmtContext ctx) 
    { 
        jFile.println("\n; " + ctx.getText());
//        jFile.println("\n; " + ctx.getText() + "\n");
        
        return visitChildren(ctx); 
    }
    @Override 
    //local variable of function!!!!!!  check ch18
    public Integer visitAssignment_stmt( PythonBParser.Assignment_stmtContext ctx)
    {
        Integer value = visit(ctx.expr());
        
        String typeIndicator = (ctx.expr().type == Predefined.integerType) ? "I"
                             : (ctx.expr().type == Predefined.realType)    ? "F"
                             :                                    "?";
        
        // Emit a field put instruction.
        jFile.println("\tputstatic\t" + programName
                           +  "/" + ctx.variable().IDENTIFIER().toString() 
                           + " " + typeIndicator);

        return value; 
    }
    @Override 
    public Integer visitIf_stmt(PythonBParser.If_stmtContext ctx)
    {
        jFile.println("L" +  String.format("%03d", index++)+ ":"); //L000:
        Integer value = visit(ctx.expr());
        jFile.print(" L" + String.format("%03d", index)); //	if_icmpgt L001:
        value = visit(ctx.stmt_list(0)); 
        int count = index + 1;
        jFile.println("\tgoto L" +  String.format("%03d", count)); //	goto L002
        jFile.print("L" +  String.format("%03d", index++)+ ":"); //L001:
        value = visit(ctx.stmt_list(1)); 
        jFile.print("L" +  String.format("%03d", index++)+ ":"); //L002:
        
        return value; 
    } 
//    public Integer visitRepeat_stmt(PythonBParser.Repeat_stmtContext ctx) 
//    { 
//        jFile.print("L" +  String.format("%03d", index++)+ ":"); //L000:
//        Integer value = visit(ctx.stmtList()); 
//        value = visit(ctx.expr());        
//        jFile.println(" L" + String.format("%03d", index)); //if_icmpt L001: the condition stmt
//        jFile.println("\ticonst_0"); // if false
//        int count = index + 1;
//        jFile.println("\tgoto L" +  String.format("%03d", count));
//        jFile.println(" L" + String.format("%03d", index++)+ ":"); // L001:
//        jFile.println("\ticonst_1"); // if true
//        jFile.println(" L" + String.format("%03d", index++)+ ":"); // L002:
//
//    	return value;
//    }
	@Override public Integer visitStmt_list(PythonBParser.Stmt_listContext ctx)
	{ 
		Integer value = visitChildren(ctx);
//		for(int i = 0; i < ctx.; i++)
//		{
//			jFile.println("num of lines:!!! :" + ctx.getChildCount());
//
////			value = visit(ctx.stmt()); 
//		}
		return value;
	}

    public Integer visitRelOpExpr(PythonBParser.RelOpExprContext ctx)
    {
    	Integer value = visitChildren(ctx);

    	// EQ_OP | NE_OP | LT_OP | LE_OP | GT_OP | GE_OP ;
    	String op = ctx.rel_op().getText();
    	String opcode;
    	if(op.equals(">"))
    	{
    		opcode = "if_icmpgt";
    	}
    	else if(op.equals("<"))
    	{
    		opcode = "if_icmplt";
    	}
//    	else if(op.equals("<"))
//    	{
//    		opcode = "if_icmplt";
//    	}
    	
    	else
    	{
    		opcode = "idk";
    	}
    	
    	jFile.print("\t" + opcode);
    	//if_icmpgt L001  ; if i > 10 goto L001

    	
    	
    	return value;
    }
    @Override 
    public Integer visitAddSubExpr(PythonBParser.AddSubExprContext ctx)
    {
        Integer value = visitChildren(ctx);
                        
        TypeSpec type1 = ctx.expr(0).type;
        TypeSpec type2 = ctx.expr(1).type;
        
        boolean integerMode =    (type1 == Predefined.integerType)
                              && (type2 == Predefined.integerType);
        boolean realMode    =    (type1 == Predefined.realType)
                              && (type2 == Predefined.realType);
        
        String op = ctx.add_sub_op().getText();
        String opcode;

        if (op.equals("+")) {
            opcode = integerMode ? "iadd"
                   : realMode    ? "fadd"
                   :               "????";
        }
        else {
            opcode = integerMode ? "isub"
                   : realMode    ? "fsub"
                   :               "????";
        }
        
        // Emit an add or subtract instruction.
        jFile.println("\t" + opcode);
        
        return value; 
    }

    @Override 
    public Integer visitMulDivExpr(PythonBParser.MulDivExprContext ctx)
    {
        Integer value = visitChildren(ctx);
                
        TypeSpec type1 = ctx.expr(0).type;
        TypeSpec type2 = ctx.expr(1).type;
        
        boolean integerMode =    (type1 == Predefined.integerType)
                              && (type2 == Predefined.integerType);
        boolean realMode    =    (type1 == Predefined.realType)
                              && (type2 == Predefined.realType);
        
        String op = ctx.mul_div_op().getText();
        String opcode;

        if (op.equals("*")) {
            opcode = integerMode ? "imul"
                   : realMode    ? "fmul"
                   :               "f???";
        }
        else {
            opcode = integerMode ? "idiv"
                   : realMode    ? "fdiv"
                   :               "????";
        }
        
        // Emit a multiply or divide instruction.
        jFile.println("\t" + opcode);
        
        return value; 
    }

    @Override 
    public Integer visitVariableExpr(PythonBParser.VariableExprContext ctx)
    {
        String variableName = ctx.variable().IDENTIFIER().toString();
        TypeSpec type = ctx.type;
        
        String typeIndicator = (type == Predefined.integerType) ? "I"
                             : (type == Predefined.realType)    ? "F"
                             :                                    "?";
        
        // Emit a field get instruction.
        jFile.println("\tgetstatic\t" + programName +
                      "/" + variableName + " " + typeIndicator);
        
        return visitChildren(ctx); 
    }
    
    @Override 
    public Integer visitSignedNumber(PythonBParser.SignedNumberContext ctx)
    {
        Integer value = visitChildren(ctx);         
        TypeSpec type = ctx.number().type;
        
        if (ctx.sign().getChild(0) == ctx.sign().SUB_OP()) {
            String opcode = (type == Predefined.integerType) ? "ineg"
                          : (type == Predefined.realType)    ? "fneg"
                          :                                    "?neg";
            
            // Emit a negate instruction.
            jFile.println("\t" + opcode);
        }
        
        return value;
    }

    @Override 
    public Integer visitIntegerConst(PythonBParser.IntegerConstContext ctx)
    {
        // Emit a load constant instruction.
        jFile.println("\tldc\t" + ctx.getText());
        
        return visitChildren(ctx); 
    }

    @Override 
    public Integer visitFloatConst(PythonBParser.FloatConstContext ctx)
    {
        // Emit a load constant instruction.
        jFile.println("\tldc\t" + ctx.getText());
        
        return visitChildren(ctx); 
    }
}