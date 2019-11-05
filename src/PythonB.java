import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.FileInputStream;
import java.io.InputStream;

public class PythonB 
{
    public static void main(String[] args) throws Exception 
    {
        String inputFile = "sample.py";
        
        // Create the input stream.
        if (args.length > 0) inputFile = args[0];
        InputStream is = (inputFile != null)
                                ? new FileInputStream(inputFile)
                                : System.in;
        
        // Create the character stream from the input stream.
        CharStream cs = CharStreams.fromStream(is);
        
        // Create a lexer which scans the character stream
        // to create a token stream.
        PythonBLexer lexer = new PythonBLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        
        // Dump the token stream.
        System.out.println("Tokens:");
        tokens.fill();
        for (Token token : tokens.getTokens()) {
            System.out.println(token.toString());
        }
        
        // Create a parser which parses the token stream
        // to create a parse tree.
        PythonBParser parser = new PythonBParser(tokens);
        ParseTree tree = parser.program();
        
        // Print the parse tree in Lisp format.
        System.out.println("\nParse tree (Lisp format):");
        System.out.println(tree.toStringTree(parser));
    }
}