package parser;

import codegenerator.AbstractXmlNode;
import codegenerator.XmlGenerator;
import parser.ast.ObjectAstNode;
import lexer.source.impl.StringSource;
import lexer.tokenizer.Tokenizer;

/**
 * Created by Igor Klemenski on 04.01.18.
 */
public class ParserWrapper {
    public static void main(String[] a) {
        Tokenizer tokenizer = new Tokenizer(new StringSource());
        ((StringSource) tokenizer.getSource()).setInputString(
                "{\"objectKey\":{\"a\":\"AAA\","
                        + "\"XD\":\"HAHA\", \"ar\":[1, 2, 3]}}"
        );
        Parser parser = new Parser(tokenizer);
        ObjectAstNode objectAstNode = parser.parse();
        System.out.println(objectAstNode);
//        XmlRenderer r = new XmlRenderer();
//        r.renderWrapper(objectAstNode);
        XmlGenerator xmlGenerator = new XmlGenerator();
        AbstractXmlNode xmlNode = xmlGenerator.generate(objectAstNode);
        int i=0;
    }
}
