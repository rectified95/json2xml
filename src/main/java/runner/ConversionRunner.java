package runner;

import codegenerator.AbstractXmlNode;
import codegenerator.XmlTreeGenerator;
import lexer.source.impl.StringSource;
import lexer.tokenizer.Tokenizer;
import parser.Parser;
import parser.ast.ObjectAstNode;

/**
 * Created by Igor Klemenski on 04.01.18.
 */
public class ConversionRunner {
    public static void main(String[] a) {
        String configString = "{\"root.objectKey.a\":\"RENAME:renamed_attr\", \"root.objectKey.b\":\"ATTRIBUTE\"," +
                " \"root.objectKey.XD\":\"SKIP\", \"root.objectKey.ar\":\"ATTRIBUTE\"," +
                " \"root.objectKey.XYZ\":\"SKIP\"}";
        String jsonString = "{\"objectKey\":{\"a\":\"AAA\", \"b\":\"BBB\","
                + "\"XD\":\"HAHA\", \"ar\":[{\"one\":1}, 2, 3]}}";
        Tokenizer tokenizer = new Tokenizer(new StringSource(jsonString));
        Parser parser = new Parser(tokenizer);
        ObjectAstNode objectAstNode = parser.parse();
        AbstractXmlNode xmlNode = XmlTreeGenerator.generate(objectAstNode, configString);
        System.out.println(xmlNode);
    }
}
