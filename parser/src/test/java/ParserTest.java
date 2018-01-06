import source.impl.StringSource;
import tokenizer.Tokenizer;
import tokenizer.token.TokenType;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by Igor Klemenski on 04.01.18.
 */
public class ParserTest {
    @Test
    public void parse() throws Exception {
    }

    @Test
    public void term() throws Exception {
        Tokenizer tokenizer = new Tokenizer(new StringSource());
        ((StringSource) tokenizer.getSource()).setInputString("\"some_string\"");
        Parser parser = new Parser(tokenizer);
        assertTrue(parser.term(TokenType.STRING));
    }

    @Test
    public void parseSimplePair() throws Exception {
        Tokenizer tokenizer = new Tokenizer(new StringSource());
        ((StringSource) tokenizer.getSource()).setInputString("\"positive\" : 766");
        Parser parser = new Parser(tokenizer);
        assertTrue(parser.pair());
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
        assertTrue(parser.object());
    }

    @Test
    public void value() throws Exception {
    }

    @Test
    public void valueString() throws Exception {
    }

    @Test
    public void valueNumber() throws Exception {
    }

    @Test
    public void valueObject() throws Exception {
    }

    @Test
    public void valueArray() throws Exception {
    }

    @Test
    public void valueKeyword() throws Exception {
    }

    @Test
    public void elements() throws Exception {
    }

    @Test
    public void elementsValue() throws Exception {
    }

    @Test
    public void elementsValueElements() throws Exception {
    }

    @Test
    public void array() throws Exception {
    }

    @Test
    public void arrayEmpty() throws Exception {
    }

    @Test
    public void arrayElements() throws Exception {
    }

    @Test
    public void object() throws Exception {
    }

    @Test
    public void objectEmpty() throws Exception {
        Tokenizer tokenizer = new Tokenizer(new StringSource());
        ((StringSource) tokenizer.getSource()).setInputString("{}");
        Parser parser = new Parser(tokenizer);
        assertTrue(parser.objectEmpty());
    }

    @Test
    public void objectMembers() throws Exception {
    }

    @Test
    public void members() throws Exception {
    }

    @Test
    public void membersPair() throws Exception {
    }

    @Test
    public void membersPairMembers() throws Exception {
    }

    @Test
    public void shouldNotParseTwoConsecutiveNumbers() throws Throwable {
        Tokenizer tokenizer = new Tokenizer(new StringSource());
        ((StringSource) tokenizer.getSource()).setInputString(
                "{\"key\" : 0666\"ss\"   , \"keyAfterNumber\" : \"random\"}"
        );
        Parser parser = new Parser(tokenizer);
        assertTrue(parser.object());
    }

}