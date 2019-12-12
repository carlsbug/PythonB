import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Hashtable;

public class PythonB 
{
	static Hashtable<String, Integer> operandStack;
    public static void main(String[] args) throws Exception 
    {
//    	operandStack = new Hashtable<Integer, String>();
        String inputFile = null;
        if (args.length >= 0) inputFile = args[0];
        InputStream is = (inputFile != null)
                                ? new FileInputStream(inputFile)
                                : System.in;
        int index = inputFile.indexOf('.');
        inputFile = inputFile.substring(0, index);
                       
        CharStream cs = CharStreams.fromStream(is);
        PythonBLexer lexer =new PythonBLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PythonBParser parser = new PythonBParser(tokens);
        ParseTree tree = parser.program();
        
        PythonB1Visitor pass1 = new PythonB1Visitor(inputFile);
        pass1.visit(tree);
        operandStack = pass1.os();
        
        PrintWriter jFile = pass1.getAssemblyFile();
        PythonB2Visitor pass2 = new PythonB2Visitor(jFile, inputFile, operandStack);
        pass2.visit(tree);
    }
}