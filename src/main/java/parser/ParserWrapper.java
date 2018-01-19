package parser;

import codegenerator.AbstractXmlNode;
import codegenerator.XmlTreeGenerator;
import lexer.source.impl.StringSource;
import lexer.tokenizer.Tokenizer;
import parser.ast.ObjectAstNode;

/**
 * Created by Igor Klemenski on 04.01.18.
 */
public class ParserWrapper {
    public static void main(String[] a) {
        Tokenizer tokenizer = new Tokenizer(new StringSource());
        ((StringSource) tokenizer.getSource()).setInputString(
                "{\"objectKey\":{\"a\":\"AAA\","
                        + "\"XD\":\"HAHA\", \"ar\":[{\"1\":1}, 2, 3]}}"
        );
        Parser parser = new Parser(tokenizer);
        ObjectAstNode objectAstNode = parser.parse();
        System.out.println(objectAstNode);
        XmlTreeGenerator xmlTreeGenerator = new XmlTreeGenerator();
        AbstractXmlNode xmlNode = xmlTreeGenerator.generate(objectAstNode);
        // TODO consider changing xmlgen to static methods
        System.out.println(xmlNode);
        int i=0;
    }
}
