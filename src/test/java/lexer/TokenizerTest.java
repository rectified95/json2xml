package lexer;

import lexer.source.impl.FileSource;
import lexer.source.impl.StringSource;
import lexer.tokenizer.Tokenizer;
import lexer.tokenizer.exception.TokenizerException;
import lexer.tokenizer.token.Token;
import lexer.tokenizer.token.TokenType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Created by Igor Klemenski on 10.12.17.
 */
// TODO add/define expected token lists in place of printing to console
public class TokenizerTest {
    // loooks like the test are run in parallel so they each need an instance of the source
//    private Tokenizer tokenizer = new Tokenizer(new StringSource());
    @Test
    void shouldTokenizeNonNestedValuesAndArraysWithEmptySimpleValues() {
        Tokenizer tokenizer = new Tokenizer(new StringSource("{\"key\":\"value\", \"key1\":\"\"," +
                " \"array1\":[\"ar_val1\", \"ar_val2\"]}"));
        // TODO rethink class design to avoid explicit casting
//        ((StringSource) tokenizer.getSource()).setInputString(
//                "{\"key\":\"value\", \"key1\":\"\"," +
//                " \"array1\":[\"ar_val1\", \"ar_val2\"]}"
//        );
        tokenizer.tokenize().stream().forEach(System.out::println);
    }

    @Test
    void shouldTokenizeNestedObjectWithBlankCharacters() {
        Tokenizer tokenizer = new Tokenizer(new StringSource("{\"composite1\": {\"nested_key\": \"nested_value\"}," +
                " \"array1\":[\"ar_val1\", \t\"ar_val2\"]}"));
//        ((StringSource) tokenizer.getSource()).setInputString(
//                        "{\"composite1\": {\"nested_key\": \"nested_value\"}," +
//                                " \"array1\":[\"ar_val1\", \t\"ar_val2\"]}"
//        );
        tokenizer.tokenize().stream().forEach(System.out::println);
    }

    @Test
    void shouldTokenizeValuesWithEscapeCharacters() {
        Tokenizer tokenizer = new Tokenizer(new StringSource());
        ((StringSource) tokenizer.getSource()).setInputString(
                "{\"key\" : \"valueWith\\\"Quote\", \"key1\":\"value with spaces\"}"
        );
        tokenizer.tokenize().stream().forEach(System.out::println);
    }

    @Test
    void shouldNotTokenizeValuesWithCorruptedEscapeCharacters() {
        Tokenizer tokenizer = new Tokenizer(new StringSource());
        ((StringSource) tokenizer.getSource()).setInputString(
                "{\"key\" : \"valueWithMissing char\\X\", \"key1\":\"value with \\ttab\"}"
        );
        Executable toExec = () -> tokenizer.tokenize();
        assertThrows(RuntimeException.class, toExec);
    }

    @Test
    void shouldTokenizePair() {
        Tokenizer tokenizer = new Tokenizer(new StringSource());
        ((StringSource) tokenizer.getSource()).setInputString("\"positive\" : 766");
        tokenizer.tokenize().stream().forEach(System.out::println);
    }

    @Test
    void shouldTokenizePositiveAndNegativeNumbers() {
        Tokenizer tokenizer = new Tokenizer(new StringSource());
        ((StringSource) tokenizer.getSource()).setInputString(
                "{\"positive\" : 766, \"negative\" : -666}"
        );
        tokenizer.tokenize().stream().forEach(System.out::println);
    }

    @Test
    void shouldTokenizeStartingWithZero() {
        Tokenizer tokenizer = new Tokenizer(new StringSource());
        ((StringSource) tokenizer.getSource()).setInputString(
                "{\"positive\" : 0, \"negative\" : -0.08, \"xxx\": 0.12}"
        );
        tokenizer.tokenize().stream().forEach(System.out::println);
    }

    @Test
    void shouldTokenizePositiveAndNegativeNumbersWithTrailingBlanks() {
        Tokenizer tokenizer = new Tokenizer(new StringSource());
        ((StringSource) tokenizer.getSource()).setInputString(
                "{\"positive\" : 666  \t , \"negative\" : -666 }"
        );
        tokenizer.tokenize().stream().forEach(System.out::println);
    }

    @Test
    void shouldTokenizeRealNumbersContainingBlanks() {
        Tokenizer tokenizer = new Tokenizer(new StringSource());
        ((StringSource) tokenizer.getSource()).setInputString(
                "{\"positive\" : 666.0698, \"negative\" : -666.67 }"
        );
        tokenizer.tokenize().stream().forEach(System.out::println);
    }

    @Test
    // this should pass, as it is the job of the parser to reject this input
    void shouldTokenizeConsecutiveNumbersContainingBlanks() {
        Tokenizer tokenizer = new Tokenizer(new StringSource());
        ((StringSource) tokenizer.getSource()).setInputString(
                "{\"positive\" : 666   6, \"negative\" : -666 }"
        );
        tokenizer.tokenize().stream().forEach(System.out::println);
    }

    @Test
    // works correctly - recognizes two token size by side- parser's job to fail it
    void shouldNotTokenizeNumbersWithLeadingZeroes() throws Throwable {
        Tokenizer tokenizer = new Tokenizer(new StringSource());
        Executable toExec = () -> {
            ((StringSource) tokenizer.getSource()).setInputString(
                    "{\"key\" : 0666\"ss\"   , \"keyAfterNumber\" : \"random\"}"
            );
            tokenizer.tokenize().stream().forEach(System.out::println);
        };
        toExec.execute();
    }

    @Test
    void shouldTokenizeKeywords() {
        Tokenizer tokenizer = new Tokenizer(new StringSource());
        ((StringSource) tokenizer.getSource()).setInputString(
                "{\"key\" : true , \"ssd\":null, \"ssss\" :1233 , \"xdxd\":false \n}"
        );
        tokenizer.tokenize().stream().forEach(System.out::println);
    }

    @Test
    void shouldTokenizeNumbersWithExponent() {
        Tokenizer tokenizer = new Tokenizer(new StringSource());
        ((StringSource) tokenizer.getSource()).setInputString(
                "{\"key\" : 666e+6   , \"keyMinus\" : 666e-9  \n}"
        );
        tokenizer.tokenize().stream().forEach(System.out::println);
    }

    @Test
    void shouldTokenizeNumbersWithExponentWithoutSignAfterExponent() {
        Tokenizer tokenizer = new Tokenizer(new StringSource());
        ((StringSource) tokenizer.getSource()).setInputString(
                "{\"key\" : 666e3   , \"keyAfterNumber\" : \"random\"}"
        );
        tokenizer.tokenize().stream().forEach(System.out::println);
    }

    @Test
    void shouldNotTokenizeNumbersWithTwoDots() {
        Tokenizer tokenizer = new Tokenizer(new StringSource("{\"key\" : 6.5.2 }"));
        Executable exec = () -> tokenizer.tokenize();
        assertThrows(TokenizerException.class, exec);
    }
    @Test
    void shouldTokenizeRealNumbersWithExponent() {
        Tokenizer tokenizer = new Tokenizer(new StringSource(
                "{\"key\" : 123.45e+64   , \"otherKey\" : -23.09E-98 }")
        );
        List<Token> expectedTokenList = Arrays.asList(
                new Token(TokenType.LCURL, "{"), new Token(TokenType.STRING, "key"),
                new Token(TokenType.COLON, ":"), new Token(TokenType.NUMBER, "123.45e+64"),
                new Token(TokenType.COMMA, ","), new Token(TokenType.STRING, "otherKey"),
                new Token(TokenType.COLON, ":"), new Token(TokenType.NUMBER, "-23.09E-98"),
                new Token(TokenType.RCURL, "}")
        );
        assertEquals(tokenizer.tokenize(), expectedTokenList);
    }

    @Test
    void shouldTokenizeZeroWithExponent() {
        Tokenizer tokenizer = new Tokenizer(new StringSource(
                "{\"key\" : 0.45e+64   , \"otherKey\" : 0E-98 }")
        );
        List<Token> expectedTokenList = Arrays.asList(
                new Token(TokenType.LCURL, "{"), new Token(TokenType.STRING, "key"),
                new Token(TokenType.COLON, ":"), new Token(TokenType.NUMBER, "0.45e+64"),
                new Token(TokenType.COMMA, ","), new Token(TokenType.STRING, "otherKey"),
                new Token(TokenType.COLON, ":"), new Token(TokenType.NUMBER, "0E-98"),
                new Token(TokenType.RCURL, "}")
        );
        assertEquals(tokenizer.tokenize(), expectedTokenList);
    }

    @Test
    void shouldNotTokenizeStringWithoutQuotes() {
        Tokenizer tokenizer = new Tokenizer(new StringSource(
                "{\"key\" : 45   , \"otherKey\" : s }")
        );
        Executable exec = () -> tokenizer.tokenize();
        assertThrows(TokenizerException.class, exec);
    }

    @Test
    void shouldNotTokenizeDollarChar() {
        Tokenizer tokenizer = new Tokenizer(new StringSource(
                "{\"key\" : $   , \"otherKey\" : s }")
        );
        Executable exec = () -> tokenizer.tokenize();
        assertThrows(TokenizerException.class, exec);
    }

    @Test
    void shouldTokenizeDeepNestedValues() {
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
        tokenizer.tokenize().stream().forEach(System.out::println);
    }

    @Test
    void shouldTokenizeFromFile() {
        try {
            Tokenizer tokenizer = new Tokenizer(new FileSource(
                    "/home/igor/Documents/projects/json2xml/src/test/java/lexer/test.json")
            );
            tokenizer.tokenize().stream().forEach(System.out::println);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}