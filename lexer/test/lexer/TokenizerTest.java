package lexer;

import source.impl.FileSource;
import source.impl.StringSource;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import tokenizer.Tokenizer;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Created by Igor Klemenski on 10.12.17.
 */
// TODO add/define expected token lists in place of printing to console
class TokenizerTest {
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
    // TODO add expect exception
    void shouldNotTokenizeValuesWithCorruptedEscapeCharacters() {
        Tokenizer tokenizer = new Tokenizer(new StringSource());
        ((StringSource) tokenizer.getSource()).setInputString(
                "{\"key\" : \"valueWithMissing char\\X\", \"key1\":\"value with \\ttab\"}"
        );
        Executable toExec = () -> {
            tokenizer.tokenize();
        };
        assertThrows(RuntimeException.class, toExec);
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
    @Disabled
    // this should pass, as it is the job of the lexer to reject this input
    void shouldNotTokenizeNumbersContainingBlanks() {
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
//        assertThrows(RuntimeException.class, toExec);
    }

    @Test
    void shouldTokenizeNumbersWithExponent() {
        Tokenizer tokenizer = new Tokenizer(new StringSource());
        ((StringSource) tokenizer.getSource()).setInputString(
                "{\"key\" : 666e+6   , \"keyMinus\" : 666e-9  " +
                        "}"
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
        Tokenizer tokenizer = new Tokenizer(new StringSource());
        ((StringSource) tokenizer.getSource()).setInputString(
                "{\"key\" : 6.5.2 }"
        );
        tokenizer.tokenize().stream().forEach(System.out::println);
    }
    @Test
    void shouldTokenizeRealNumbersWithExponent() {
        Tokenizer tokenizer = new Tokenizer(new StringSource());
        ((StringSource) tokenizer.getSource()).setInputString(
                "{\"key\" : 123.45e+64   , \"otherKey\" : -23.09E-98 }"
        );
        tokenizer.tokenize().stream().forEach(System.out::println);
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
                    "/home/igor/Documents/projects/json2xml/lexer/test/lexer/test.json")
            );
            tokenizer.tokenize().stream().forEach(System.out::println);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}