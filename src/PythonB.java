import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;

public class PythonB 
{
    public static void main(String[] args) throws Exception 
    {
    	
        String inputFile = null;
        if (args.length >= 0) inputFile = args[0];
        InputStream is = (inputFile != null)
                                ? new FileInputStream(inputFile)
                                : System.in;
        int index = inputFile.indexOf('.');
        inputFile = inputFile.substring(0, index);
                       
        System.out.println(inputFile);
        CharStream cs = CharStreams.fromStream(is);
        PythonBLexer lexer = new PythonBLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PythonBParser parser = new PythonBParser(tokens);
        ParseTree tree = parser.program();
        
        PythonB1Visitor pass1 = new PythonB1Visitor(inputFile);
        pass1.visit(tree);
        
        PrintWriter jFile = pass1.getAssemblyFile();

        PythonB2Visitor pass2 = new PythonB2Visitor(jFile);
        pass2.visit(tree);
    }
}