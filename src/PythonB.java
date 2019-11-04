
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import java.io.FileInputStream;
import java.io.InputStream;

public class PythonB 
{
	public static void main(String[] args) throws Exception 
	{
		String inputFile = null;

		if (args.length > 0) inputFile = args[0];
		InputStream is = (inputFile != null)
				? new FileInputStream(inputFile)
						: System.in;

				CharStream cs = CharStreams.fromStream(is);
				PythonBLexer lexer = new PythonBLexer(cs);
				CommonTokenStream tokens = new CommonTokenStream(lexer);

				System.out.println("Tokens:");
				tokens.fill();
				for (Token token : tokens.getTokens())
				{
					System.out.println(token.toString());
				}

				PythonBParser parser = new PythonBParser(tokens);
				ParseTree tree = parser.program();

				CompilerVisitor compiler = new CompilerVisitor();
				compiler.visit(tree);
	}
}
