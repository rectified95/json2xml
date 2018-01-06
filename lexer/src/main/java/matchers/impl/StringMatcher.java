package matchers.impl;

import matchers.Matcher;
import source.Source;
import tokenizer.exception.TokenizerException;
import tokenizer.token.Token;
import tokenizer.token.TokenType;

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
    public Token match(Source source) {
        StringBuilder sb = new StringBuilder();
        try {
            char nextChar = source.getNext();
            while (nextChar != '"') {
                if (nextChar == '\\') {
                    sb.append(nextChar);
                    nextChar = source.getNext();
                    if (!controlCharacterSet.contains(nextChar)) {
                        throw new TokenizerException("invalid json string - missing control character after escape" +
                                        " '\\' character on line " + source.getLineNumber() + ":" + source.getLineIdx());
                    }
                }
                // escaping quotes handled here
                sb.append(nextChar);
                nextChar = source.getNext();
            }
        } catch (TokenizerException e) {
            System.out.println(e);
        }
        return new Token(TokenType.STRING, sb.toString());
    }
}
