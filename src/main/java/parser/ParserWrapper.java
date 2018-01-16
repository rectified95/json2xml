package parser;

import parser.ast.ObjectAstNode;
import lexer.source.impl.StringSource;
import lexer.tokenizer.Tokenizer;
import parser.ast.StringAstNode;
import renderer.XmlRenderer;

/**
 * Created by Igor Klemenski on 04.01.18.
 */
public class ParserWrapper {
    public static void main(String[] a) {
        Tokenizer tokenizer = new Tokenizer(new StringSource());
        ((StringSource) tokenizer.getSource()).setInputString(
                "{\"ar\":[0, {\"neg\":\"-34.5\",\"zero\":true}, 1, [3,2,3]], \"objectKey\":{\"a\":\"AAA\","
                        + "\"XD\":\"HAHA\"}}"
        );
        Parser parser = new Parser(tokenizer);
        ObjectAstNode objectAstNode = parser.parse();
        System.out.println(objectAstNode);
        XmlRenderer r = new XmlRenderer();
        r.renderWrapper(objectAstNode);
    }
}
