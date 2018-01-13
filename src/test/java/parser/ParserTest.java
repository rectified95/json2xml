package parser;

import parser.ast.ObjectAstNode;
import parser.exception.ParserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import lexer.source.impl.StringSource;
import lexer.tokenizer.Tokenizer;
import lexer.tokenizer.token.TokenType;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by Igor Klemenski on 04.01.18.
 */
public class ParserTest {
    @Test
    public void term() throws Exception {
        Tokenizer tokenizer = new Tokenizer(new StringSource());
        ((StringSource) tokenizer.getSource()).setInputString("\"some_string\"");
        Parser parser = new Parser(tokenizer);
        parser.getNext();
        assertTrue(parser.term(TokenType.STRING));
    }

    @Test
    public void parseSimplePair() throws Exception {
        Tokenizer tokenizer = new Tokenizer(new StringSource());
        ((StringSource) tokenizer.getSource()).setInputString("\"positive\" : 766");
        Parser parser = new Parser(tokenizer);
        parser.getNext();
        assertTrue(parser.parsePair(1) != null);
    }

    @Test
    public void shouldParseSimpleKV() {
        Tokenizer tokenizer = new Tokenizer(new StringSource());
        ((StringSource) tokenizer.getSource()).setInputString(
                "{\"positive\" : 0, \"negative\" : -0.08, \"xxx\": 0.12}"
        );
        Parser parser = new Parser(tokenizer);
        ObjectAstNode objectAstNode = parser.parse();
        System.out.println(objectAstNode);
        assertTrue(objectAstNode != null);
    }

    @Test
    public void shouldParseArray() {
        Tokenizer tokenizer = new Tokenizer(new StringSource());
        ((StringSource) tokenizer.getSource()).setInputString(
                "{\"positive\" : [1,2,3]}"
        );
        Parser parser = new Parser(tokenizer);
        ObjectAstNode objectAstNode = parser.parse();
        System.out.println(objectAstNode);
        assertTrue(objectAstNode != null);
    }

    @Test
    public void parseSimpleObject() throws Exception {
        Tokenizer tokenizer = new Tokenizer(new StringSource());
        ((StringSource) tokenizer.getSource()).setInputString(
                "{\n" +
                        "    \"glossary\": {\n" +
                        "        \"title\": \"example glossary\",\n" +
                        "\t\t\"GlossDiv\": {\n" +
                        "            \"title\": \"S\",\n" +
                        "\t\t\t\"GlossList\": {\n" +
                        "                \"GlossEntry\": {\n" +
                        "                    \"ID\": \"SGML\",\n" +
                        "\t\t\t\t\t\"SortAs\": \"SGML\",\n" +
                        "\t\t\t\t\t\"GlossTerm\": \"Standard Generalized Markup Language\",\n" +
                        "\t\t\t\t\t\"Acronym\": \"SGML\",\n" +
                        "\t\t\t\t\t\"Abbrev\": \"ISO 8879:1986\",\n" +
                        "\t\t\t\t\t\"GlossDef\": {\n" +
                        "                        \"para\": \"A meta-markup language, used to create markup languages such as DocBook.\",\n" +
                        "\t\t\t\t\t\t\"GlossSeeAlso\": [\"GML\", \"XML\"]\n" +
                        "                    },\n" +
                        "\t\t\t\t\t\"GlossSee\": \"markup\"\n" +
                        "                }\n" +
                        "            }\n" +
                        "        }\n" +
                        "    }\n" +
                        "}"
        );
        Parser parser = new Parser(tokenizer);
        ObjectAstNode objectAstNode = parser.parse();
        System.out.println(objectAstNode);
        assertTrue(objectAstNode != null);
    }

    @Test
    public void objectEmpty() throws Exception {
        Tokenizer tokenizer = new Tokenizer(new StringSource());
        ((StringSource) tokenizer.getSource()).setInputString("{}");
        Parser parser = new Parser(tokenizer);
        assertTrue(parser.parse() != null);
    }

    @Test
    public void shouldNotParseTwoConsecutiveNumbers() throws Throwable {
        Tokenizer tokenizer = new Tokenizer(new StringSource());
        ((StringSource) tokenizer.getSource()).setInputString(
                "{\"key\" : 0666\"ss\"   , \"keyAfterNumber\" : \"random\"}"
        );
        Parser parser = new Parser(tokenizer);
        Executable exec = () -> parser.parse();
        assertThrows(ParserException.class, exec);
    }
}