package matchers.impl;

import tokenizer.Token;
import tokenizer.TokenType;
import matchers.Matcher;
import source.Source;
import tokenizer.TokenizerException;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Igor Klemenski on 10.12.17.
 */
public class StringMatcher implements Matcher {
    private static final Set<Character> controlCharacterSet =
            new TreeSet<>(Arrays.asList(
                    '"', '\\', '/', 'b', 'f', 'n', 'r', 't')
            );

    @Override
    // TODO add hex lexing
    // TODO account for premature string end case eg. {"key":"val"
    public Token match(Source source) {
        StringBuilder sb = new StringBuilder();
        char nextChar = source.getNext();
        while (nextChar != '"') {
            if (nextChar == '\\') {
                sb.append(nextChar);
                nextChar = source.getNext();
                if (!controlCharacterSet.contains(nextChar)) {
                    throw new TokenizerException(
                            "invalid json string - missing control character after escape '\\' character"
                    );
                }
            }
            // escaping quotes handled here
            sb.append(nextChar);
            nextChar = source.getNext();
        }
        return new Token(TokenType.STRING, sb.toString());
    }
}
