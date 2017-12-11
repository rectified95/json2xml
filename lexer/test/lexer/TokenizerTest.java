package lexer;

import lexer.source.impl.StringSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Created by Igor Klemenski on 10.12.17.
 */
// TODO add/define expected token lists in place of printing to console
class TokenizerTest {
    private static Tokenizer tokenizer = new Tokenizer(new StringSource());

    @AfterEach
    void clear() {
        tokenizer.reset(new StringSource());
    }
    @Test
    void shouldTokenizeNonNestedValuesAndArraysWithEmptySimpleValues() {
        // TODO rethink class design to avoid explicit casting
        ((StringSource) tokenizer.getSource()).setInputString(
                "{\"key\":\"value\", \"key1\":\"\"," +
                " \"array1\":[\"ar_val1\", \"ar_val2\"]}"
        );
        tokenizer.tokenize().stream().forEach(System.out::println);
//        tokenizer.reset(new StringSource());
    }

    @Test
    void shouldTokenizeNestedObjectWithBlankCharacters() {
        ((StringSource) tokenizer.getSource()).setInputString(
                        "{\"composite1\": {\"nested_key\": \"nested_value\"}," +
                                " \"array1\":[\"ar_val1\", \t\"ar_val2\"]}"
        );
        tokenizer.tokenize().stream().forEach(System.out::println);
//        tokenizer.reset(new StringSource());
    }

    @Test
    void shouldTokenizeValuesWithEscapeCharacters() {
        ((StringSource) tokenizer.getSource()).setInputString(
                "{\"key\" : \"valueWith\\\"Quote\", \"key1\":\"value with spaces\"}"
        );
        tokenizer.tokenize().stream().forEach(System.out::println);
//        tokenizer.reset(new StringSource());
    }

    @Test
    // TODO add expect exception
    void shouldNotTokenizeValuesWithCorruptedEscapeCharacters() {
        ((StringSource) tokenizer.getSource()).setInputString(
                "{\"key\" : \"valueWithMissing char\\X\", \"key1\":\"value with \\ttab\"}"
        );
        tokenizer.tokenize().stream().forEach(System.out::println);
//        tokenizer.reset(new StringSource());
    }

    @Test
    void shouldTokenizePositiveAndNegativeNumbers() {
        ((StringSource) tokenizer.getSource()).setInputString(
                "{\"positive\" : 766, \"negative\" : -666}"
        );
        tokenizer.tokenize().stream().forEach(System.out::println);
//        tokenizer.reset(new StringSource());
    }

    @Test
    void shouldTokenizePositiveAndNegativeNumbersWithTrailingBlanks() {
        ((StringSource) tokenizer.getSource()).setInputString(
                "{\"positive\" : 666  \t , \"negative\" : -666 }"
        );
        tokenizer.tokenize().stream().forEach(System.out::println);
//        tokenizer.reset(new StringSource());
    }

    @Test
    void shouldTokenizeRealNumbersContainingBlanks() {
        ((StringSource) tokenizer.getSource()).setInputString(
                "{\"positive\" : 666.0698, \"negative\" : -666.67 }"
        );
        tokenizer.tokenize().stream().forEach(System.out::println);
//        tokenizer.reset(new StringSource());
    }

    @Test
    void shouldNotTokenizeNumbersContainingBlanks() {
        ((StringSource) tokenizer.getSource()).setInputString(
                "{\"positive\" : 666   6, \"negative\" : -666 }"
        );
        tokenizer.tokenize().stream().forEach(System.out::println);
//        tokenizer.reset(new StringSource());
    }

    @Test
    void shouldNotTokenizeNumbersWithLeadingZeroes() {
        ((StringSource) tokenizer.getSource()).setInputString(
                "{\"key\" : 0666   , \"keyAfterNumber\" : \"random\"}"
        );
        tokenizer.tokenize().stream().forEach(System.out::println);
//        tokenizer.reset(new StringSource());
    }

    @Test
    void shouldTokenizeNumbersWithExponent() {
        ((StringSource) tokenizer.getSource()).setInputString(
                "{\"key\" : 666e+6   , \"keyAfterNumber\" : \"random\"}"
        );
        tokenizer.tokenize().stream().forEach(System.out::println);
//        tokenizer.reset(new StringSource());
    }

    @Test
    void shouldTokenizeRealNumbersWithExponent() {
        ((StringSource) tokenizer.getSource()).setInputString(
                "{\"key\" : 123.45e+64   , \"otherKey\" : -23.09E-98 }"
        );
        tokenizer.tokenize().stream().forEach(System.out::println);
//        tokenizer.reset(new StringSource());
    }
}