import ast.ObjectAstNode;
import source.impl.StringSource;
import tokenizer.Tokenizer;

/**
 * Created by Igor Klemenski on 04.01.18.
 */
public class ParserWrapper {
    public static void main(String[] a) {
        Tokenizer tokenizer = new Tokenizer(new StringSource());
        ((StringSource) tokenizer.getSource()).setInputString(
                "{\"positive\" : 0, \"negative\" : -0.08, \"xxx\": 0.12}"
        );
        Parser parser = new Parser(tokenizer);
        ObjectAstNode objectAstNode = parser.parse();
        System.out.println(objectAstNode.toString());
    }
}
