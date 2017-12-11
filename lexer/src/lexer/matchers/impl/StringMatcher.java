package lexer.matchers.impl;

import lexer.Token;
import lexer.TokenType;
import lexer.matchers.Matcher;
import lexer.source.Source;

import java.util.Arrays;
import java.util.Collections;
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
    // TODO account for remature string end case eg. {"key":"val"
    public Token match(Source source) {
        StringBuilder sb = new StringBuilder();
        char nextChar = source.getNext();
        while (nextChar != '"') {
            if (nextChar == '\\') {
                sb.append(nextChar);
                nextChar = source.getNext();
                if (!controlCharacterSet.contains(nextChar)) {
                    // TODO define own exception class
                    throw new RuntimeException(
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
