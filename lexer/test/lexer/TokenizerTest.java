package lexer;

import lexer.source.impl.StringSource;
import org.junit.jupiter.api.Test;

/**
 * Created by Igor Klemenski on 10.12.17.
 */
class TokenizerTest {
    private static Tokenizer tokenizer = new Tokenizer(new StringSource());

    @Test
    void shouldTokenizeNonNestedValuesAndArraysWithEmptySimpleValues() {
        // TODO rethink class design to avoid explicit casting
        ((StringSource) tokenizer.getSource()).setInputString(
                "{\"key\":\"value\", \"key1\":\"\"," +
                " \"array1\":[\"ar_val1\", \"ar_val2\"]}"
        );
        tokenizer.tokenize().stream().forEach(System.out::println);
        tokenizer.reset(new StringSource());
    }

    @Test
    void shouldTokenizeNestedObjectWithBlankCharacters() {
        ((StringSource) tokenizer.getSource()).setInputString(
                        "{\"composite1\": {\"nested_key\": \"nested_value\"}," +
                                " \"array1\":[\"ar_val1\", \t\"ar_val2\"]}"
        );
        tokenizer.tokenize().stream().forEach(System.out::println);
        tokenizer.reset(new StringSource());
    }

    @Test
    void shouldTokenizeValuesWithEscapeCharacters() {
        ((StringSource) tokenizer.getSource()).setInputString(
                "{\"key\" : \"valueWith\\\"Quote\", \"key1\":\"value with spaces\"}"
        );
        tokenizer.tokenize().stream().forEach(System.out::println);
        tokenizer.reset(new StringSource());
    }
}